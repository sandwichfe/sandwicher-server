package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.req.NoteGroupReq;
import com.lww.littlenote.service.NoteGroupService;
import com.lww.littlenote.vo.NoteGroupVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lww
 * @since 2025-01-22 00:00:35
 */
@ApiLittleNoteRestController
@RequestMapping("/noteGroup")
public class NoteGroupController {

    @Resource
    private NoteGroupService noteGroupService;

    @PostMapping("/listNoteGroup")
    public ResponseResult<IPage<NoteGroupVo>> listNoteGroup(@Valid PageVo pageVo) {
        return ResultUtil.success(noteGroupService.listNoteGroup(pageVo));
    }

    @GetMapping("/getNoteGroup")
    public ResponseResult<NoteGroupVo> getNote(Long id) {
        return ResultUtil.success(noteGroupService.getNoteGroupDetail(id));
    }


    @PostMapping("/addNoteGroup")
    public ResponseResult<Long> addNote(@RequestBody NoteGroupReq noteReq) {
        return ResultUtil.success(noteGroupService.addNoteGroup(noteReq));
    }

    @PostMapping("/editNoteGroup")
    public ResponseResult<Void> editNote(@RequestBody NoteGroupReq noteReq) {
        noteGroupService.editNoteGroup(noteReq);
        return ResultUtil.success();
    }

    @GetMapping("/deleteNoteGroup")
    public ResponseResult<Void> deleteNote(Long id) {
        noteGroupService.deleteNoteGroup(id);
        return ResultUtil.success();
    }


}
