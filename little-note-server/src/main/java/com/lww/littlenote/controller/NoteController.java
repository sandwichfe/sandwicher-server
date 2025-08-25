package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.dep.resources.server.config.utils.SecurityUserUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.dto.NoteDto;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.service.NoteService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author lww
 */
@ApiLittleNoteRestController
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping("/listNote")
    public ResponseResult<Page<Note>> listNote(@Valid NoteDto noteDto) {
        // 在 Controller/Service 中直接调用
        Long userId = SecurityUserUtils.getUserId();
        noteDto.setUserId(userId);
        Page<Note> pages = noteService.listNote(noteDto);
        return ResultUtil.success(pages);
    }

    @GetMapping("/getNote")
    public Note getNote(Long id) {
        return noteService.getById(id);
    }


    @PostMapping("/addNote")
    public ResponseResult<Long> addNote(@RequestBody Note note) {
        Long userId = SecurityUserUtils.getUserId();
        note.setUserId(userId);
        note.setUpdateTime(LocalDateTime.now());
        noteService.save(note);
        Long id = note.getId();
        return ResultUtil.success(id);

    }

    @PostMapping("/editNote")
    public ResponseResult<Void> editNote(@RequestBody Note note) {
        Long userId = SecurityUserUtils.getUserId();
        note.setUserId(userId);
        note.setUpdateTime(LocalDateTime.now());
        noteService.updateById(note);
        return ResultUtil.success();
    }

    @GetMapping("/deleteNote")
    public ResponseResult<Void> deleteNote(Note note) {
        noteService.removeById(note);
        return ResultUtil.success();
    }

}
