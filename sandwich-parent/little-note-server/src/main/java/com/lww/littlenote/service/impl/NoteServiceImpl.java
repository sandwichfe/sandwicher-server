package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.dto.NoteDto;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.service.NoteService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-06-13
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {


    @Resource
    private NoteMapper noteMapper;

    @Override
    public Page<Note> listNote(NoteDto noteDto) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Note::getId, Note::getTitle, Note::getCreateTime, Note::getUpdateTime)
                .eq(noteDto.getGroupId() != null, Note::getGroupId, noteDto.getGroupId())
                .orderByDesc(Note::getUpdateTime);
        return noteMapper.selectPage(new Page<>(noteDto.getPageNum(), noteDto.getPageSize()), wrapper);
    }
}
