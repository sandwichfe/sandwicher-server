package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.exception.AppException;
import com.lww.common.web.vo.PageVo;
import com.lww.littlenote.entity.NoteGroup;
import com.lww.littlenote.mapper.NoteGroupMapper;
import com.lww.littlenote.req.NoteGroupReq;
import com.lww.littlenote.service.NoteGroupService;
import com.lww.littlenote.vo.NoteGroupVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public IPage<NoteGroupVo> listNoteGroup(PageVo noteDto) {
        LambdaQueryWrapper<NoteGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .orderByDesc(NoteGroup::getId);
        Page<NoteGroup> noteGroupPage = noteGroupMapper.selectPage(new Page<>(noteDto.getPageNum(), noteDto.getPageSize()), wrapper);
        return noteGroupPage.convert(noteGroup -> CustomBeanUtils.copyProperties(noteGroup, NoteGroupVo.class));
    }

    @Override
    public NoteGroupVo getNoteGroupDetail(Long id) {
        NoteGroup noteGroup = this.getById(id);
        if (noteGroup == null) {
            throw new AppException("数据已不存在！");
        }
        return CustomBeanUtils.copyProperties(noteGroup, NoteGroupVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addNoteGroup(NoteGroupReq noteGroupReq) {
        NoteGroup noteGroup = CustomBeanUtils.copyProperties(noteGroupReq, NoteGroup.class);
        this.save(noteGroup);
        return noteGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editNoteGroup(NoteGroupReq noteGroupReq) {
        NoteGroup noteGroup = CustomBeanUtils.copyProperties(noteGroupReq, NoteGroup.class);
        boolean success = this.updateById(noteGroup);
        if (!success) {
            throw new AppException("数据已不存在！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNoteGroup(Long id) {
        boolean success = this.removeById(id);
        if (!success) {
            throw new AppException("数据已不存在！");
        }
    }
}
