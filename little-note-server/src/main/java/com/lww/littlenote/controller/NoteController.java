package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.req.NoteReq;
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
    public ResponseResult<IPage<NoteVo>> listNote(@Valid NoteQueryReq noteQueryReq) {
        return ResultUtil.success(noteService.listNote(noteQueryReq));
    }

    @GetMapping("/getNote")
    public ResponseResult<NoteVo> getNote(Long id) {
        return ResultUtil.success(noteService.getCurrentUserNoteDetail(id));
    }


    @PostMapping("/addNote")
    public ResponseResult<Long> addNote(@RequestBody NoteReq noteReq) {
        return ResultUtil.success(noteService.addNote(noteReq));
    }

    @PostMapping("/editNote")
    public ResponseResult<Void> editNote(@RequestBody NoteReq noteReq) {
        noteService.editNote(noteReq);
        return ResultUtil.success();
    }

    @GetMapping("/deleteNote")
    public ResponseResult<Void> deleteNote(Long id) {
        noteService.deleteNote(id);
        return ResultUtil.success();
    }

}
