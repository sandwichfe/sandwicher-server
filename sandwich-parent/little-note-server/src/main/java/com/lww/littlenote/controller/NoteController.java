package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.service.NoteService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/note")
public class NoteController {


    @Resource
    private NoteService noteService;

    @PostMapping("/listNote")
    public ResponseResult<Page<Note>> listNote(@Valid PageVo pageVo) {
        Page<Note> pages = noteService.listNote(pageVo);
        return ResultUtil.success(pages);
    }

    @PostMapping("/getNote")
    public Note getNote(Long id) {
        return noteService.getById(id);
    }


    @PostMapping("/addNote")
    public ResponseResult<Long> addNote(Note note) {
        note.setUpdateTime(LocalDateTime.now());
        noteService.save(note);
        Long id = note.getId();
        return ResultUtil.success(id);

    }

    @PostMapping("/editNote")
    public ResponseResult<Void> listNote(Note note) {
        noteService.saveOrUpdate(note);
        return ResultUtil.success();
    }


    @PostMapping("/deleteNote")
    public ResponseResult<Void> deleteNote(Note note) {
        noteService.removeById(note);
        return ResultUtil.success();
    }

}
