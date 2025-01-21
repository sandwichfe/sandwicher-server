package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.service.NoteService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author lww
 */
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

    @GetMapping("/getNote")
    public Note getNote(Long id) {
        return noteService.getById(id);
    }


    @PostMapping("/addNote")
    public ResponseResult<Long> addNote(@RequestBody Note note) {
        note.setUpdateTime(LocalDateTime.now());
        noteService.save(note);
        Long id = note.getId();
        return ResultUtil.success(id);

    }

    @PostMapping("/editNote")
    public ResponseResult<Void> editNote(@RequestBody Note note) {
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
