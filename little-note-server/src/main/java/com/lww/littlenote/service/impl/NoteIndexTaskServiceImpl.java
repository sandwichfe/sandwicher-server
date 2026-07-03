package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.entity.NoteIndexTask;
import com.lww.littlenote.mapper.NoteIndexTaskMapper;
import com.lww.littlenote.mapper.NoteMapper;
import com.lww.littlenote.service.NoteEsService;
import com.lww.littlenote.service.NoteIndexTaskService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 笔记 ES 索引同步任务服务实现。
 */
@Service
public class NoteIndexTaskServiceImpl extends ServiceImpl<NoteIndexTaskMapper, NoteIndexTask> implements NoteIndexTaskService {

    private static final String STATUS_WAIT = "WAIT";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAIL = "FAIL";
    private static final int MAX_RETRY_COUNT = 5;
    private static final int BATCH_SIZE = 20;
    private static final int MAX_ERROR_MSG_LENGTH = 500;

    @Resource
    private NoteMapper noteMapper;

    @Resource
    private NoteEsService noteEsService;

    @Override
    public void createTask(Long noteId, String action) {
        NoteIndexTask task = new NoteIndexTask();
        task.setNoteId(noteId);
        task.setAction(action);
        task.setStatus(STATUS_WAIT);
        task.setRetryCount(0);
        this.save(task);
    }

    @Override
    public void processPendingTasks() {
        List<NoteIndexTask> tasks = this.list(new LambdaQueryWrapper<NoteIndexTask>()
                .in(NoteIndexTask::getStatus, STATUS_WAIT, STATUS_FAIL)
                .lt(NoteIndexTask::getRetryCount, MAX_RETRY_COUNT)
                .orderByAsc(NoteIndexTask::getCreateTime)
                .last("limit " + BATCH_SIZE));

        for (NoteIndexTask task : tasks) {
            processTask(task);
        }
    }

    private void processTask(NoteIndexTask task) {
        try {
            if (ACTION_DELETE.equals(task.getAction())) {
                noteEsService.deleteIndex(task.getNoteId());
            } else {
                Note note = noteMapper.selectById(task.getNoteId());
                if (note == null) {
                    // UPDATE/CREATE 处理时数据库已无记录，按删除 ES 索引兜底，保证任务幂等。
                    noteEsService.deleteIndex(task.getNoteId());
                } else {
                    noteEsService.saveOrUpdateIndex(note);
                }
            }
            markSuccess(task);
        } catch (Exception e) {
            markFail(task, e);
        }
    }

    private void markSuccess(NoteIndexTask task) {
        task.setStatus(STATUS_SUCCESS);
        task.setErrorMsg(null);
        task.setUpdateTime(LocalDateTime.now());
        this.updateById(task);
    }

    private void markFail(NoteIndexTask task, Exception e) {
        task.setStatus(STATUS_FAIL);
        task.setRetryCount(task.getRetryCount() == null ? 1 : task.getRetryCount() + 1);
        task.setErrorMsg(shortErrorMsg(e));
        task.setUpdateTime(LocalDateTime.now());
        this.updateById(task);
    }

    private String shortErrorMsg(Exception e) {
        String message = e.getMessage();
        if (message == null) {
            message = e.getClass().getSimpleName();
        }
        return message.length() > MAX_ERROR_MSG_LENGTH ? message.substring(0, MAX_ERROR_MSG_LENGTH) : message;
    }
}
