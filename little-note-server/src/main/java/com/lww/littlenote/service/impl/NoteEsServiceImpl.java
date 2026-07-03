package com.lww.littlenote.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.exception.AppException;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.entity.es.NoteEsDocument;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.service.NoteEsService;
import com.lww.littlenote.utils.NoteContentEncryptUtil;
import com.lww.littlenote.vo.NoteVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 笔记 ES 搜索和索引维护实现。
 * @author lww
 * @since 2026-07-03
 */
@Slf4j
@Service
public class NoteEsServiceImpl implements NoteEsService {

    private static final String NOTE_INDEX = "little_note_note";
    private static final String HIGHLIGHT_PRE_TAG = "<em>";
    private static final String HIGHLIGHT_POST_TAG = "</em>";

    private static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    private static final int ES_QUERY_ALL_SIZE = 10000;

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Override
    public IPage<NoteVo> search(NoteQueryReq noteQueryReq, Long userId) {
        long pageNum = noteQueryReq.getPageNum();
        long pageSize = noteQueryReq.getPageSize();
        boolean queryAll = pageSize < 0;
        // pageSize = -1 means query all; ES size cannot be negative.
        int from = queryAll ? 0 : Math.toIntExact((Math.max(pageNum, 1) - 1) * pageSize);
        int size = queryAll ? ES_QUERY_ALL_SIZE : Math.toIntExact(pageSize);

        try {
            if (!indexExists()) {
                return new Page<>(pageNum, pageSize, 0);
            }
            SearchResponse<NoteEsDocument> response = elasticsearchClient.search(request -> {
                request.index(NOTE_INDEX)
                        .from(from)
                        .size(size)
                        .query(query -> query.bool(bool -> {
                            bool.must(must -> must.bool(keywordBool -> keywordBool
                                    // 普通字段保留全文分词搜索，ngram 子字段用于补充命中 59 这类子串。
                                    .should(should -> should.multiMatch(multiMatch -> multiMatch
                                            .fields("title^3", "content")
                                            .query(noteQueryReq.getKeyword())))
                                    .should(should -> should.multiMatch(multiMatch -> multiMatch
                                            .fields("title.ngram^1.5", "content.ngram^0.5")
                                            .query(noteQueryReq.getKeyword())))
                                    .minimumShouldMatch("1")));
                            bool.filter(filter -> filter.term(term -> term
                                    .field("userId")
                                    .value(userId)));
                            if (noteQueryReq.getGroupId() != null) {
                                bool.filter(filter -> filter.term(term -> term
                                        .field("groupId")
                                        .value(noteQueryReq.getGroupId())));
                            }
                            return bool;
                        }))
                        .sort(sort -> sort.field(field -> field
                                // updateTime is auto-mapped as text in ES, sort by keyword sub-field.
                                .field("updateTime.keyword")
                                .order(SortOrder.Desc)))
                        .highlight(highlight -> highlight
                                .preTags(HIGHLIGHT_PRE_TAG)
                                .postTags(HIGHLIGHT_POST_TAG)
                                // ngram 子字段命中时，也回填到原字段高亮，避免有 hit 但没有 highlight。
                                .fields("title", field -> field.matchedFields("title", "title.ngram"))
                                .fields("content", field -> field.matchedFields("content", "content.ngram")));
                return request;
            }, NoteEsDocument.class);

            Page<NoteVo> page = new Page<>(pageNum, pageSize, response.hits().total() == null ? 0 : response.hits().total().value());
            List<NoteVo> records = new ArrayList<>();
            for (Hit<NoteEsDocument> hit : response.hits().hits()) {
                NoteEsDocument document = hit.source();
                if (document == null) {
                    continue;
                }
                NoteVo noteVo = toNoteVo(document);
                List<String> highlightTitle = hit.highlight().get("title");
                List<String> highlightContent = hit.highlight().get("content");
                if (highlightTitle != null && !highlightTitle.isEmpty()) {
                    noteVo.setHighlightTitle(highlightTitle.get(0));
                }
                if (highlightContent != null && !highlightContent.isEmpty()) {
                    noteVo.setHighlightContent(highlightContent.get(0));
                }
                records.add(noteVo);
            }
            page.setRecords(records);
            return page;
        } catch (Exception e) {
            log.error("笔记搜索失败: {}", noteQueryReq, e);
            throw new AppException("笔记搜索失败");
        }
    }

    @Override
    public void saveOrUpdateIndex(Note note) {
        try {
            elasticsearchClient.index(index -> index
                    .index(NOTE_INDEX)
                    .id(String.valueOf(note.getId()))
                    .document(toDocument(note)));
        } catch (Exception e) {
            log.error("保存笔记索引失败: {}", note, e);
            throw new AppException("笔记索引保存失败");
        }
    }

    @Override
    public void deleteIndex(Long noteId) {
        try {
            boolean exists = elasticsearchClient.exists(existsRequest -> existsRequest
                    .index(NOTE_INDEX)
                    .id(String.valueOf(noteId))).value();
            if (exists) {
                elasticsearchClient.delete(delete -> delete
                        .index(NOTE_INDEX)
                        .id(String.valueOf(noteId)));
            }
        } catch (Exception e) {
            throw new AppException("笔记索引删除失败");
        }
    }

    private boolean indexExists() throws Exception {
        return elasticsearchClient.indices()
                .exists(existsRequest -> existsRequest.index(NOTE_INDEX))
                .value();
    }

    private NoteEsDocument toDocument(Note note) {
        NoteEsDocument document = new NoteEsDocument();
        document.setId(note.getId());
        document.setTitle(note.getTitle());
        // ES 需要写入明文正文，保证关键词可以命中前端加密后的笔记内容。
        document.setContent(NoteContentEncryptUtil.decryptContent(note.getContent()));
        document.setGroupId(note.getGroupId());
        document.setUserId(note.getUserId());
        document.setCreateTime(note.getCreateTime().format(DateTimeFormatter.ofPattern(PATTERN_DATE_TIME)));
        document.setUpdateTime(note.getUpdateTime().format(DateTimeFormatter.ofPattern(PATTERN_DATE_TIME)));
        return document;
    }

    private NoteVo toNoteVo(NoteEsDocument document) {
        NoteVo noteVo = new NoteVo();
        noteVo.setId(document.getId());
        noteVo.setTitle(document.getTitle());
        noteVo.setContent(document.getContent());
        noteVo.setGroupId(document.getGroupId());
        noteVo.setUserId(document.getUserId());
        noteVo.setCreateTime(LocalDateTime.parse(document.getCreateTime(), DateTimeFormatter.ofPattern(PATTERN_DATE_TIME)));
        noteVo.setUpdateTime(LocalDateTime.parse(document.getUpdateTime(), DateTimeFormatter.ofPattern(PATTERN_DATE_TIME)));
        return noteVo;
    }
}
