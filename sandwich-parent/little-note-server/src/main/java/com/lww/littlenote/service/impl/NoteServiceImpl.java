package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.web.Vo.PageVo;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.service.NoteService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

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
    public Page<Note> listNote(PageVo pageVo) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Note::getId, Note::getTitle, Note::getCreateTime, Note::getUpdateTime);
        wrapper.orderByDesc(Note::getUpdateTime);
        return noteMapper.selectPage(new Page<>(pageVo.getPageNum(), pageVo.getPageSize()), wrapper);
    }
}
