package com.lww.littlenote.service.impl;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.resources_server.utils.SecurityUserUtils;
import com.lww.web.support.utils.CustomBeanUtils;
import com.lww.web.support.exception.AppException;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.req.NoteQueryReq;
import com.lww.littlenote.req.NoteReq;
import com.lww.littlenote.service.NoteEsService;
import com.lww.littlenote.service.NoteIndexTaskService;
import com.lww.littlenote.service.NoteService;
import com.lww.littlenote.vo.NoteVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.lww.littlenote.service.NoteIndexTaskService.ACTION_CREATE;
import static com.lww.littlenote.service.NoteIndexTaskService.ACTION_DELETE;
import static com.lww.littlenote.service.NoteIndexTaskService.ACTION_UPDATE;

/**
 * <p>
 * 笔记服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-06-13
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Resource
    private NoteMapper noteMapper;

    @Resource
    private NoteEsService noteEsService;

    @Resource
    private NoteIndexTaskService noteIndexTaskService;

    @Override
    public IPage<NoteVo> listNote(NoteQueryReq noteQueryReq) {
        Long userId = SecurityUserUtils.getUserId();
        if (StringUtils.hasText(noteQueryReq.getKeyword())) {
            return noteEsService.search(noteQueryReq, userId);
        }

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
        // ES 同步走 outbox 任务，避免 ES 短暂异常影响笔记保存。
        noteIndexTaskService.createTask(note.getId(), ACTION_CREATE);
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
        // 仅记录同步动作，后台任务会重新读取数据库最新笔记并覆盖写入 ES。
        noteIndexTaskService.createTask(noteReq.getId(), ACTION_UPDATE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNote(Long id) {
        boolean success = this.remove(currentUserNoteWrapper(id));
        if (!success) {
            throw new AppException("数据已不存在！");
        }
        // 删除动作同样异步处理，ES 文档不存在时后台任务会按成功处理。
        noteIndexTaskService.createTask(id, ACTION_DELETE);
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
