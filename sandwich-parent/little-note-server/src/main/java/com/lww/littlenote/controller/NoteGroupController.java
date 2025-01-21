package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.dto.NoteDto;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.entity.NoteGroup;
import com.lww.littlenote.service.NoteGroupService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lww
 * @since 2025-01-22 00:00:35
 */
@RestController
@RequestMapping("/noteGroup")
public class NoteGroupController {

    @Resource
    private NoteGroupService noteGroupService;

    @PostMapping("/listNoteGroup")
    public ResponseResult<Page<NoteGroup>> listNoteGroup(@Valid PageVo pageVo) {
        Page<NoteGroup> pages = noteGroupService.listNoteGroup(pageVo);
        return ResultUtil.success(pages);
    }

    @GetMapping("/getNoteGroup")
    public NoteGroup getNote(Long id) {
        return noteGroupService.getById(id);
    }


    @PostMapping("/addNoteGroup")
    public ResponseResult<Long> addNote(@RequestBody NoteGroup note) {
        noteGroupService.save(note);
        Long id = note.getId();
        return ResultUtil.success(id);

    }

    @PostMapping("/editNoteGroup")
    public ResponseResult<Void> editNote(@RequestBody NoteGroup note) {
        noteGroupService.updateById(note);
        return ResultUtil.success();
    }

    @GetMapping("/deleteNoteGroup")
    public ResponseResult<Void> deleteNote(Note note) {
        noteGroupService.removeById(note);
        return ResultUtil.success();
    }


}
