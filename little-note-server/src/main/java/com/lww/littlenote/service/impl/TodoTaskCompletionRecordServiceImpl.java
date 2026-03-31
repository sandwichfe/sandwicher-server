package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.entity.TodoTaskCompletionRecord;
import com.lww.littlenote.mapper.TodoTaskCompletionRecordMapper;
import com.lww.littlenote.service.TodoTaskCompletionRecordService;
import com.lww.littlenote.vo.TodoTaskCompletionRecordVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoTaskCompletionRecordServiceImpl extends ServiceImpl<TodoTaskCompletionRecordMapper, TodoTaskCompletionRecord>
        implements TodoTaskCompletionRecordService {

    @Override
    public List<TodoTaskCompletionRecordVO> listTaskRecords(Long taskId, Long userId) {
        return this.list(new LambdaQueryWrapper<TodoTaskCompletionRecord>()
                        .eq(TodoTaskCompletionRecord::getTaskId, taskId)
                        .eq(TodoTaskCompletionRecord::getUserId, userId)
                        .orderByDesc(TodoTaskCompletionRecord::getCompletedAt)
                        .orderByDesc(TodoTaskCompletionRecord::getId))
                .stream()
                .map(record -> {
                    TodoTaskCompletionRecordVO vo = new TodoTaskCompletionRecordVO();
                    vo.setId(record.getId());
                    vo.setTaskId(record.getTaskId());
                    vo.setCompletedSequence(record.getCompletedSequence());
                    vo.setCompletedAt(record.getCompletedAt());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeByTaskId(Long taskId) {
        this.remove(new LambdaQueryWrapper<TodoTaskCompletionRecord>()
                .eq(TodoTaskCompletionRecord::getTaskId, taskId));
    }
}
