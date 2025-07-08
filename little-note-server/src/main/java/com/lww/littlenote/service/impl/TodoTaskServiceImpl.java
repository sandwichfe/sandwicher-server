package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.dto.TodoTaskDto;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.mapper.TodoTaskMapper;
import com.lww.littlenote.service.TodoTaskService;
import com.lww.littlenote.service.TodoUserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 统一任务表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Service
public class TodoTaskServiceImpl extends ServiceImpl<TodoTaskMapper, TodoTask> implements TodoTaskService {

    @Autowired
    private TodoUserPointsService todoUserPointsService;

    @Override
    public Page<TodoTask> listTasks(TodoTaskDto todoTaskDto) {
        Page<TodoTask> page = new Page<>(todoTaskDto.getPageNum(), todoTaskDto.getPageSize());
        LambdaQueryWrapper<TodoTask> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(TodoTask::getUserId, todoTaskDto.getUserId())
                .eq(StringUtils.hasText(todoTaskDto.getTaskType()), TodoTask::getTaskType, todoTaskDto.getTaskType())
                .eq(StringUtils.hasText(todoTaskDto.getCategory()), TodoTask::getCategory, todoTaskDto.getCategory())
                .eq(todoTaskDto.getTodoDate() != null, TodoTask::getTodoDate, todoTaskDto.getTodoDate())
                .orderByDesc(TodoTask::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean completeTaskOnce(Long taskId, Long userId) {
        TodoTask task = this.getById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            return false;
        }
        
        // 检查是否已完成
        if (task.getCompletedCount() >= task.getTargetCount()) {
            return false;
        }
        
        // 增加完成次数
        task.setCompletedCount(task.getCompletedCount() + 1);
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateBy(userId);
        
        boolean updated = this.updateById(task);
        
        // 奖励积分
        if (updated && task.getPoints() != null && task.getPoints() > 0) {
            todoUserPointsService.addPoints(userId, task.getPoints(), "task_complete", taskId, 
                    "完成任务：" + task.getContent());
        }
        
        return updated;
    }

    @Override
    public Long copyToDaily(Long originalTaskId, Long userId) {
        TodoTask originalTask = this.getById(originalTaskId);
        if (originalTask == null || !originalTask.getUserId().equals(userId)) {
            return null;
        }
        
        TodoTask dailyTask = new TodoTask();
        dailyTask.setContent(originalTask.getContent());
        dailyTask.setTaskType(originalTask.getTaskType());
        dailyTask.setCategory("daily");
        dailyTask.setPoints(originalTask.getPoints());
        dailyTask.setEncouragement(originalTask.getEncouragement());
        dailyTask.setTargetCount(originalTask.getTargetCount());
        dailyTask.setCompletedCount(0);
        dailyTask.setOriginalTaskId(originalTaskId);
        dailyTask.setTodoDate(LocalDate.now());
        dailyTask.setUserId(userId);
        dailyTask.setCreateTime(LocalDateTime.now());
        dailyTask.setCreateBy(userId);
        
        boolean saved = this.save(dailyTask);
        return saved ? dailyTask.getId() : null;
    }

    @Override
    public Object getTaskStats(Long userId, String category) {
        LambdaQueryWrapper<TodoTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TodoTask::getUserId, userId);
        
        if (StringUtils.hasText(category)) {
            queryWrapper.eq(TodoTask::getCategory, category);
        }
        
        if ("daily".equals(category)) {
            queryWrapper.eq(TodoTask::getTodoDate, LocalDate.now());
        }
        
        long totalTasks = this.count(queryWrapper);
        
        // 统计已完成的任务（completedCount >= targetCount）
        queryWrapper.apply("completed_count >= target_count");
        long completedTasks = this.count(queryWrapper);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", totalTasks);
        stats.put("completedTasks", completedTasks);
        stats.put("pendingTasks", totalTasks - completedTasks);
        stats.put("completionRate", totalTasks > 0 ? (double) completedTasks / totalTasks : 0.0);
        
        return stats;
    }
}