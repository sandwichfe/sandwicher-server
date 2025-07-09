package com.lww.littlenote.controller;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.dep.resources.server.config.utils.SecurityUserUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.dto.TodoRewardDto;
import com.lww.littlenote.dto.TodoTaskDto;
import com.lww.littlenote.entity.TodoRewardItem;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.entity.TodoUserPoints;
import com.lww.littlenote.entity.TodoUserReward;
import com.lww.littlenote.service.TodoRewardItemService;
import com.lww.littlenote.service.TodoTaskService;
import com.lww.littlenote.service.TodoUserPointsService;
import com.lww.littlenote.service.TodoUserRewardService;
import com.lww.littlenote.vo.TaskStatsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Todo任务管理 前端控制器
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoTaskService todoTaskService;
    
    private final TodoUserPointsService todoUserPointsService;
    
    private final TodoRewardItemService todoRewardItemService;
    
    private final TodoUserRewardService todoUserRewardService;

    /**
     * 分页查询任务列表
     */
    @PostMapping("/tasks/list")
    public ResponseResult<Page<TodoTask>> listTasks(@RequestBody TodoTaskDto todoTaskDto) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskDto.setUserId(userId);
        Page<TodoTask> page = todoTaskService.listTasks(todoTaskDto);
        return ResultUtil.success(page);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/tasks/{id}")
    public ResponseResult<TodoTask> getTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = todoTaskService.getById(id);
        if (task == null || !task.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        return ResultUtil.success(task);
    }

    /**
     * 添加任务
     */
    @PostMapping("/tasks")
    public ResponseResult<TodoTask> addTask(@RequestBody TodoTask task) {
        Long userId = SecurityUserUtils.getUserId();
        task.setUserId(userId);
        task.setCompletedCount(0);
        task.setCreateTime(LocalDateTime.now());
        task.setCreateBy(userId);
        
        if (task.getTargetCount() == null || task.getTargetCount() <= 0) {
            task.setTargetCount(1);
        }
        
        boolean saved = todoTaskService.save(task);
        return saved ? ResultUtil.success(task) : ResultUtil.error("添加失败");
    }

    /**
     * 编辑任务
     */
    @PutMapping("/tasks/{id}")
    public ResponseResult<TodoTask> editTask(@PathVariable Long id, @RequestBody TodoTask task) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask existingTask = todoTaskService.getById(id);
        if (existingTask == null || !existingTask.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        
        task.setId(id);
        task.setUserId(userId);
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateBy(userId);
        
        boolean updated = todoTaskService.updateById(task);
        return updated ? ResultUtil.success(task) : ResultUtil.error("更新失败");
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseResult<Void> deleteTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = todoTaskService.getById(id);
        if (task == null || !task.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        todoTaskService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 完成任务一次
     */
    @PostMapping("/tasks/{id}/complete")
    public ResponseResult<Void> completeTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskService.completeTaskOnce(id, userId);
        return ResultUtil.success();
    }

    /**
     * 复制任务到每日待办
     */
    @PostMapping("/tasks/{id}/copy-to-daily")
    public ResponseResult<Long> copyToDaily(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        Long newTaskId = todoTaskService.copyToDaily(id, userId);
        return newTaskId != null ? ResultUtil.success(newTaskId) : ResultUtil.error("复制失败");
    }

    /**
     * 获取任务统计
     */
    @GetMapping("/tasks/stats")
    public ResponseResult<TaskStatsVO> getTaskStats(@RequestParam(required = false) String category) {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.getTaskStats(userId, category));
    }

    /**
     * 获取用户积分信息
     */
    @GetMapping("/points")
    public ResponseResult<TodoUserPoints> getUserPoints() {
        Long userId = SecurityUserUtils.getUserId();
        TodoUserPoints userPoints = todoUserPointsService.getUserPoints(userId);
        return ResultUtil.success(userPoints);
    }

    /**
     * 分页查询奖励商品列表
     */
    @PostMapping("/rewards/list")
    public ResponseResult<Page<TodoRewardItem>> listRewards(@RequestBody TodoRewardDto todoRewardDto) {
        Long userId = SecurityUserUtils.getUserId();
        todoRewardDto.setUserId(userId);
        Page<TodoRewardItem> page = todoRewardItemService.listRewards(todoRewardDto);
        return ResultUtil.success(page);
    }

    /**
     * 获取奖励详情
     */
    @GetMapping("/rewards/{id}")
    public ResponseResult<TodoRewardItem> getReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem reward = todoRewardItemService.getById(id);
        if (reward == null || !reward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        return ResultUtil.success(reward);
    }

    /**
     * 添加奖励商品
     */
    @PostMapping("/rewards")
    public ResponseResult<Void> addReward(@RequestBody TodoRewardItem reward) {
        Long userId = SecurityUserUtils.getUserId();
        reward.setUserId(userId);
        reward.setStatus(1);
        reward.setCreateTime(LocalDateTime.now());
        reward.setCreateBy(userId);
        
        todoRewardItemService.save(reward);
        return ResultUtil.success();
    }

    /**
     * 编辑奖励商品
     */
    @PutMapping("/rewards/{id}")
    public ResponseResult<Void> editReward(@PathVariable Long id, @RequestBody TodoRewardItem reward) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem existingReward = todoRewardItemService.getById(id);
        if (existingReward == null || !existingReward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        
        reward.setId(id);
        reward.setUserId(userId);
        reward.setUpdateTime(LocalDateTime.now());
        reward.setUpdateBy(userId);
        
        todoRewardItemService.updateById(reward);
        return ResultUtil.success();
    }

    /**
     * 删除奖励商品
     */
    @DeleteMapping("/rewards/{id}")
    public ResponseResult<Void> deleteReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem reward = todoRewardItemService.getById(id);
        if (reward == null || !reward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        
        todoRewardItemService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 兑换奖励
     */
    @PostMapping("/rewards/{id}/exchange")
    public ResponseResult<Void> exchangeReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        todoRewardItemService.exchangeReward(id, userId);
        return ResultUtil.success();
    }

    /**
     * 分页查询用户奖励列表
     */
    @GetMapping("/user-rewards")
    public ResponseResult<Page<TodoUserReward>> listUserRewards(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String status) {
        Long userId = SecurityUserUtils.getUserId();
        Page<TodoUserReward> page = todoUserRewardService.listUserRewards(userId, pageNum, pageSize, status);
        return ResultUtil.success(page);
    }

    /**
     * 使用奖励
     */
    @PostMapping("/user-rewards/{id}/use")
    public ResponseResult<Void> useReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        todoUserRewardService.useReward(id, userId);
        return ResultUtil.success();
    }
}