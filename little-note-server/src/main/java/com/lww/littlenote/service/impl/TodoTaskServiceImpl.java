package com.lww.littlenote.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.mapper.TodoTaskMapper;
import com.lww.littlenote.req.TodoTaskQueryReq;
import com.lww.littlenote.service.TodoTaskService;
import com.lww.littlenote.service.TodoUserPointsService;
import com.lww.littlenote.vo.TaskStatsVO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 统一任务表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Service
@RequiredArgsConstructor
public class TodoTaskServiceImpl extends ServiceImpl<TodoTaskMapper, TodoTask> implements TodoTaskService {

    private final TodoUserPointsService todoUserPointsService;

    @Override
    public Page<TodoTask> listTasks(TodoTaskQueryReq todoTaskQueryReq) {
        Page<TodoTask> page = new Page<>(todoTaskQueryReq.getPageNum(), todoTaskQueryReq.getPageSize());
        LambdaQueryWrapper<TodoTask> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(TodoTask::getUserId, todoTaskQueryReq.getUserId())
                .eq(StringUtils.hasText(todoTaskQueryReq.getTaskType()), TodoTask::getTaskType, todoTaskQueryReq.getTaskType())
                .eq(StringUtils.hasText(todoTaskQueryReq.getCategory()), TodoTask::getCategory, todoTaskQueryReq.getCategory())
                .eq(todoTaskQueryReq.getTodoDate() != null, TodoTask::getTodoDate, todoTaskQueryReq.getTodoDate())
                .orderByDesc(TodoTask::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public TaskStatsVO getTaskStats(Long userId, String category) {
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


        TaskStatsVO taskStatsVO = new TaskStatsVO(totalTasks, completedTasks);
        taskStatsVO.setTotalTasks(totalTasks);
        taskStatsVO.setCompletedTasks(totalTasks);
        taskStatsVO.setPendingTasks(totalTasks - completedTasks);
        taskStatsVO.setCompletionRate(totalTasks > 0 ? (double) completedTasks / totalTasks : 0.0);

        return taskStatsVO;
    }
}