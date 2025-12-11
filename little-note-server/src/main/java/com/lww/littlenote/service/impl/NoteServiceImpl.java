package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.service.NoteService;
import com.lww.littlenote.vo.NoteVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    public IPage<NoteVo> listNote(NoteQueryReq noteQueryReq) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Note::getId, Note::getTitle, Note::getCreateTime, Note::getUpdateTime,Note::getGroupId,Note::getUserId)
                .eq(noteQueryReq.getGroupId() != null, Note::getGroupId, noteQueryReq.getGroupId())
                .eq(noteQueryReq.getUserId() != null, Note::getUserId, noteQueryReq.getUserId())
                .orderByDesc(Note::getUpdateTime);

        Page<Note> notePage = noteMapper.selectPage(new Page<>(noteQueryReq.getPageNum(), noteQueryReq.getPageSize()), wrapper);
        return notePage.convert(note -> {
            NoteVo vo = new NoteVo();
            BeanUtils.copyProperties(note, vo);
            return vo;
        });
    }
}
