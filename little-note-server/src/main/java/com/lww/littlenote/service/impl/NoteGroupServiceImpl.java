package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.NoteGroup;
import com.lww.littlenote.mapper.NoteGroupMapper;
import com.lww.littlenote.service.NoteGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-22 00:00:35
 */
@Service
public class NoteGroupServiceImpl extends ServiceImpl<NoteGroupMapper, NoteGroup> implements NoteGroupService {

    @Resource
    private NoteGroupMapper noteGroupMapper;

    @Override
    public Page<NoteGroup> listNoteGroup(PageVo noteDto) {
        LambdaQueryWrapper<NoteGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .orderByDesc(NoteGroup::getId);
        return noteGroupMapper.selectPage(new Page<>(noteDto.getPageNum(), noteDto.getPageSize()), wrapper);
    }
}
