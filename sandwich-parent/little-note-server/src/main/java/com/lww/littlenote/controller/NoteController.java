package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.service.NoteService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/note")
public class NoteController {

    private final String key = "8F6B2CK33DZE20A08O74C231B47AC8F9";

    @Resource
    private NoteService noteService;

    @PostMapping("/listNote")
    public ResponseResult<Page<Note>> listNote(@Valid PageVo pageVo) {
        Page<Note> pages = noteService.listNote(pageVo);
        return ResultUtil.success(pages);
    }

    @PostMapping("/getNote")
    public Note getNote(Long id) throws Exception {

        Note note = noteService.getById(id);

        // note.setContent(AESUtil.encrypt(note.getContent(), key));
        return note;
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
