package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.TodoTaskCompletionRecord;
import com.lww.littlenote.vo.TodoTaskCompletionRecordVO;

import java.util.List;

public interface TodoTaskCompletionRecordService extends IService<TodoTaskCompletionRecord> {

    List<TodoTaskCompletionRecordVO> listTaskRecords(Long taskId, Long userId);

    void removeByTaskId(Long taskId);
}
