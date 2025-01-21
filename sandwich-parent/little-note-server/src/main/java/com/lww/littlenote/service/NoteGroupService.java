package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.NoteGroup;
import jakarta.validation.Valid;

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
     * @param noteDto
     * @return
     */
    Page<NoteGroup> listNoteGroup(@Valid PageVo noteDto);
}
