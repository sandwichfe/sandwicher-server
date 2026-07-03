package com.lww.littlenote.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.vo.NoteVo;

/**
 * 笔记 ES 搜索和索引维护。
 */
public interface NoteEsService {

    /**
     * 按关键词搜索当前用户的笔记。
     */
    IPage<NoteVo> search(NoteQueryReq noteQueryReq, Long userId);

    /**
     * 覆盖写入笔记索引。
     */
    void saveOrUpdateIndex(Note note);

    /**
     * 删除笔记索引。
     */
    void deleteIndex(Long noteId);
}
