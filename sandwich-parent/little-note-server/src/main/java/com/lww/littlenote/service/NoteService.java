package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.dto.NoteDto;
import com.lww.littlenote.entity.Note;

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
     * @param pageVo
     * @return
     */
    Page<Note> listNote(NoteDto pageVo);
}
