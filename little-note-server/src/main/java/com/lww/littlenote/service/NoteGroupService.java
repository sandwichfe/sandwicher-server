package com.lww.littlenote.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.NoteGroup;
import com.lww.littlenote.req.NoteGroupReq;
import com.lww.littlenote.vo.NoteGroupVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lww
 * @since 2025-01-22 00:00:35
 */
public interface NoteGroupService extends IService<NoteGroup> {

    /**
     * listNoteGroup
     * @param noteDto noteDto
     * @return Page
     */
    IPage<NoteGroupVo> listNoteGroup(PageVo noteDto);

    /**
     * 获取笔记分组详情
     *
     * @param id 分组ID
     * @return 分组详情
     */
    NoteGroupVo getNoteGroupDetail(Long id);

    /**
     * 新增笔记分组
     *
     * @param noteGroupReq 分组请求参数
     * @return 分组ID
     */
    Long addNoteGroup(NoteGroupReq noteGroupReq);

    /**
     * 编辑笔记分组
     *
     * @param noteGroupReq 分组请求参数
     */
    void editNoteGroup(NoteGroupReq noteGroupReq);

    /**
     * 删除笔记分组
     *
     * @param id 分组ID
     */
    void deleteNoteGroup(Long id);
}
