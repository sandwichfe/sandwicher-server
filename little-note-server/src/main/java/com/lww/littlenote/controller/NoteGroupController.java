package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.entity.NoteGroup;
import com.lww.littlenote.req.NoteGroupReq;
import com.lww.littlenote.service.NoteGroupService;
import com.lww.littlenote.vo.NoteGroupVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Page<NoteGroup> pages = noteGroupService.listNoteGroup(pageVo);
        IPage<NoteGroupVo> pageVoList = pages.convert(item -> CustomBeanUtils.copyProperties(item, NoteGroupVo.class));
        return ResultUtil.success(pageVoList);
    }

    @GetMapping("/getNoteGroup")
    public ResponseResult<NoteGroupVo> getNote(Long id) {
        NoteGroup noteGroup = noteGroupService.getById(id);
        return ResultUtil.success(CustomBeanUtils.copyProperties(noteGroup, NoteGroupVo.class));
    }


    @PostMapping("/addNoteGroup")
    public ResponseResult<Long> addNote(@RequestBody NoteGroupReq noteReq) {
        NoteGroup note = new NoteGroup();
        BeanUtils.copyProperties(noteReq, note);
        noteGroupService.save(note);
        Long id = note.getId();
        return ResultUtil.success(id);

    }

    @PostMapping("/editNoteGroup")
    public ResponseResult<Void> editNote(@RequestBody NoteGroupReq noteReq) {
        NoteGroup note = new NoteGroup();
        BeanUtils.copyProperties(noteReq, note);
        noteGroupService.updateById(note);
        return ResultUtil.success();
    }

    @GetMapping("/deleteNoteGroup")
    public ResponseResult<Void> deleteNote(Long id) {
        noteGroupService.removeById(id);
        return ResultUtil.success();
    }


}
