package com.lww.littlenote.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.req.NoteReq;
import com.lww.littlenote.vo.NoteVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lww
 * @since 2024-06-13
 */
public interface NoteService extends IService<Note> {

    /**
     * listNote
     *
     * @param pageVo pageVo
     * @return Page
     */
    IPage<NoteVo> listNote(NoteQueryReq pageVo);

    /**
     * 获取笔记详情
     *
     * @param id 笔记ID
     * @return 笔记Vo
     */
    NoteVo getCurrentUserNoteDetail(Long id);

    /**
     * 新增笔记
     *
     * @param noteReq 笔记请求参数
     * @return 笔记ID
     */
    Long addNote(NoteReq noteReq);

    /**
     * 编辑笔记
     *
     * @param noteReq 笔记请求参数
     */
    void editNote(NoteReq noteReq);

    /**
     * 删除笔记
     *
     * @param id 笔记请求参数
     */
    void deleteNote(Long id);
}
