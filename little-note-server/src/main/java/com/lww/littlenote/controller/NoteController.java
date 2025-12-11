package com.lww.littlenote.controller;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.resources_server.utils.SecurityUserUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.dto.NoteDto;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.service.NoteService;
import com.lww.littlenote.vo.NoteVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lww
 */
@ApiLittleNoteRestController
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping("/listNote")
    public ResponseResult<IPage<NoteVo>> listNote(@Valid NoteDto noteDto) {
        Long userId = SecurityUserUtils.getUserId();
        noteDto.setUserId(userId);
        IPage<NoteVo> pages = noteService.listNote(noteDto);
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
