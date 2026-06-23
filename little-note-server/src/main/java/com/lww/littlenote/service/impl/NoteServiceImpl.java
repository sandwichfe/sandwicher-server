package com.lww.littlenote.service.impl;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.resources_server.utils.SecurityUserUtils;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.exception.AppException;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.req.NoteReq;
import com.lww.littlenote.service.NoteService;
import com.lww.littlenote.vo.NoteVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 鏈嶅姟瀹炵幇绫?
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
        Long userId = SecurityUserUtils.getUserId();
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Note::getId, Note::getTitle, Note::getCreateTime, Note::getUpdateTime, Note::getGroupId, Note::getUserId)
                .eq(noteQueryReq.getGroupId() != null, Note::getGroupId, noteQueryReq.getGroupId())
                .eq(Note::getUserId, userId)
                .orderByDesc(Note::getUpdateTime);

        Page<Note> notePage = noteMapper.selectPage(new Page<>(noteQueryReq.getPageNum(), noteQueryReq.getPageSize()), wrapper);
        return notePage.convert(note -> CustomBeanUtils.copyProperties(note, NoteVo.class));
    }

    @Override
    public NoteVo getCurrentUserNoteDetail(Long id) {
        Note note = this.getCurrentUserNote(id);
        if (note == null) {
            throw new AppException("数据已不存在！");
        }
        return CustomBeanUtils.copyProperties(note, NoteVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addNote(NoteReq noteReq) {
        Note note = CustomBeanUtils.copyProperties(noteReq, Note.class);
        note.setUserId(SecurityUserUtils.getUserId());
        note.setUpdateTime(LocalDateTime.now());
        this.save(note);
        return note.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editNote(NoteReq noteReq) {
        Note note = CustomBeanUtils.copyProperties(noteReq, Note.class);
        note.setUserId(SecurityUserUtils.getUserId());
        note.setUpdateTime(LocalDateTime.now());

        boolean success = this.update(note, currentUserNoteWrapper(noteReq.getId()));
        if (!success) {
            throw new AppException("数据已不存在！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNote(Long id) {
        boolean success = this.remove(currentUserNoteWrapper(id));
        if (!success) {
            throw new AppException("数据已不存在！");
        }
    }

    private Note getCurrentUserNote(Long id) {
        return this.getOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getId, id)
                .eq(Note::getUserId, SecurityUserUtils.getUserId()));
    }

    private LambdaQueryWrapper<Note> currentUserNoteWrapper(Long id) {
        return new LambdaQueryWrapper<Note>()
                .eq(Note::getId, id)
                .eq(Note::getUserId, SecurityUserUtils.getUserId());
    }
}
