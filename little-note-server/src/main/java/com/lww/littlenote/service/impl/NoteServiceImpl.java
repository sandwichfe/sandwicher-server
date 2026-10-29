package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.resources_server.utils.SecurityUserUtils;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.req.NoteReq;
import com.lww.littlenote.service.NoteEsService;
import com.lww.littlenote.service.NoteIndexTaskService;
import com.lww.littlenote.service.NoteService;
import com.lww.littlenote.utils.NoteContentEncryptUtil;
import com.lww.littlenote.vo.NoteVo;
import com.lww.web.support.exception.AppException;
import com.lww.web.support.utils.CustomBeanUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.lww.littlenote.service.NoteIndexTaskService.*;

/**
 * <p>
 * 笔记服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-06-13
 */
@Slf4j
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    /**
     * 正文解密迁移的分页大小，避免一次性把全部笔记加载进内存。
     */
    private static final int DECRYPT_PAGE_SIZE = 500;

    @Resource
    private NoteMapper noteMapper;

    @Resource
    private NoteEsService noteEsService;

    @Resource
    private NoteIndexTaskService noteIndexTaskService;

    @Override
    public IPage<NoteVo> listNote(NoteQueryReq noteQueryReq) {
        Long userId = SecurityUserUtils.getUserId();
        if (StringUtils.hasText(noteQueryReq.getKeyword())) {
            return noteEsService.search(noteQueryReq, userId);
        }

        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Note::getId, Note::getTitle, Note::getContent, Note::getCreateTime, Note::getUpdateTime,
                        Note::getGroupId, Note::getUserId)
                .eq(noteQueryReq.getGroupId() != null, Note::getGroupId, noteQueryReq.getGroupId())
                .eq(Note::getUserId, userId)
                .orderByDesc(Note::getUpdateTime);

        Page<Note> notePage = noteMapper.selectPage(new Page<>(noteQueryReq.getPageNum(), noteQueryReq.getPageSize()), wrapper);
        return notePage.convert(note -> CustomBeanUtils.copyProperties(note, NoteVo.class));
    }

    @Override
    public NoteVo getCurrentUserNoteDetail(Long id) {
        Note note = this.getCurrentUserNote(id);
        if (note == null) {
            throw new AppException("数据已不存在！");
        }
        return CustomBeanUtils.copyProperties(note, NoteVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addNote(NoteReq noteReq) {
        Note note = CustomBeanUtils.copyProperties(noteReq, Note.class);
        note.setUserId(SecurityUserUtils.getUserId());
        note.setUpdateTime(LocalDateTime.now());
        this.save(note);
        // ES 同步走 outbox 任务，避免 ES 短暂异常影响笔记保存。
        noteIndexTaskService.createTask(note.getId(), ACTION_CREATE);
        return note.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editNote(NoteReq noteReq) {
        Note note = CustomBeanUtils.copyProperties(noteReq, Note.class);
        note.setUserId(SecurityUserUtils.getUserId());
        note.setUpdateTime(LocalDateTime.now());

        boolean success = this.update(note, currentUserNoteWrapper(noteReq.getId()));
        if (!success) {
            throw new AppException("数据已不存在！");
        }
        // 仅记录同步动作，后台任务会重新读取数据库最新笔记并覆盖写入 ES。
        noteIndexTaskService.createTask(noteReq.getId(), ACTION_UPDATE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNote(Long id) {
        boolean success = this.remove(currentUserNoteWrapper(id));
        if (!success) {
            throw new AppException("数据已不存在！");
        }
        // 删除动作同样异步处理，ES 文档不存在时后台任务会按成功处理。
        noteIndexTaskService.createTask(id, ACTION_DELETE);
    }

    @Override
    public int decryptAllNoteContent() {
        int pageNum = 1;
        int successCount = 0;
        while (true) {
            // 走投影查询只取必要字段，且不查总数，减少迁移时的数据库压力。
            Page<Note> notePage = noteMapper.selectPage(new Page<>(pageNum, DECRYPT_PAGE_SIZE, false),
                    new LambdaQueryWrapper<Note>().select(Note::getId, Note::getContent));
            if (notePage.getRecords().isEmpty()) {
                break;
            }
            for (Note note : notePage.getRecords()) {
                if (decryptAndUpdateContent(note)) {
                    successCount++;
                }
            }
            pageNum++;
        }
        log.info("笔记正文解密迁移完成，成功解密 {} 条", successCount);
        return successCount;
    }

    /**
     * 解密单条笔记正文并回写数据库，同时投递 ES 索引刷新任务。
     *
     * @return 是否发生了实际的解密更新
     */
    private boolean decryptAndUpdateContent(Note note) {
        String plainContent;
        try {
            plainContent = NoteContentEncryptUtil.decryptContent(note.getContent());
        } catch (Exception e) {
            // 已是明文的笔记解密会失败，属于正常情况；脏数据同理跳过，不影响其余记录。
            log.debug("笔记 {} 正文无需解密，跳过", note.getId());
            return false;
        }
        if (!StringUtils.hasText(plainContent) || plainContent.equals(note.getContent())) {
            return false;
        }
        // 只更新正文，避免把其他字段覆盖成空值。
        Note updateNote = new Note();
        updateNote.setId(note.getId());
        updateNote.setContent(plainContent);
        noteMapper.updateById(updateNote);
        updateNote.setUpdateTime(note.getUpdateTime());
        // 复用 outbox 任务，由定时任务重新读取数据库最新正文刷新 ES 索引。
        noteIndexTaskService.createTask(note.getId(), ACTION_UPDATE);
        return true;
    }

    private Note getCurrentUserNote(Long id) {
        return this.getOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getId, id)
                .eq(Note::getUserId, SecurityUserUtils.getUserId()));
    }

    private LambdaQueryWrapper<Note> currentUserNoteWrapper(Long id) {
        return new LambdaQueryWrapper<Note>()
                .eq(Note::getId, id)
                .eq(Note::getUserId, SecurityUserUtils.getUserId());
    }
}
