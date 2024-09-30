package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.Vo.PageVo;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.service.NoteService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public HashMap addNote(Note note) {
        note.setUpdateTime(LocalDateTime.now());
        noteService.save(note);
        Long id = note.getId();
        HashMap<String, Long> ret = new HashMap<>();
        ret.put("code", 200L);
        ret.put("id", id);
        return ret;

    }

    @PostMapping("/editNote")
    public String listNote(Note note) {
        noteService.saveOrUpdate(note);
        return "ok";
    }


    @PostMapping("/deleteNote")
    public String deleteNote(Note note) {
        noteService.removeById(note);
        return "ok";
    }

}
