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
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult listTasks(@RequestBody TodoTaskDto todoTaskDto) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskDto.setUserId(userId);
        Page<TodoTask> page = todoTaskService.listTasks(todoTaskDto);
        return ResultUtil.success(page);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/tasks/{id}")
    public ResponseResult getTask(@PathVariable Long id) {
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
    public ResponseResult addTask(@RequestBody TodoTask task) {
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
    public ResponseResult editTask(@PathVariable Long id, @RequestBody TodoTask task) {
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
    public ResponseResult deleteTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = todoTaskService.getById(id);
        if (task == null || !task.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        
        boolean deleted = todoTaskService.removeById(id);
        return deleted ? ResultUtil.success("删除成功") : ResultUtil.error("删除失败");
    }

    /**
     * 完成任务一次
     */
    @PostMapping("/tasks/{id}/complete")
    public ResponseResult completeTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        boolean completed = todoTaskService.completeTaskOnce(id, userId);
        return completed ? ResultUtil.success("任务完成") : ResultUtil.error("完成失败");
    }

    /**
     * 复制任务到每日待办
     */
    @PostMapping("/tasks/{id}/copy-to-daily")
    public ResponseResult copyToDaily(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        Long newTaskId = todoTaskService.copyToDaily(id, userId);
        return newTaskId != null ? ResultUtil.success(newTaskId) : ResultUtil.error("复制失败");
    }

    /**
     * 获取任务统计
     */
    @GetMapping("/tasks/stats")
    public ResponseResult getTaskStats(@RequestParam(required = false) String category) {
        Long userId = SecurityUserUtils.getUserId();
        Object stats = todoTaskService.getTaskStats(userId, category);
        return ResultUtil.success(stats);
    }

    /**
     * 获取用户积分信息
     */
    @GetMapping("/points")
    public ResponseResult getUserPoints() {
        Long userId = SecurityUserUtils.getUserId();
        TodoUserPoints userPoints = todoUserPointsService.getUserPoints(userId);
        return ResultUtil.success(userPoints);
    }

    /**
     * 分页查询奖励商品列表
     */
    @PostMapping("/rewards/list")
    public ResponseResult listRewards(@RequestBody TodoRewardDto todoRewardDto) {
        Long userId = SecurityUserUtils.getUserId();
        todoRewardDto.setUserId(userId);
        Page<TodoRewardItem> page = todoRewardItemService.listRewards(todoRewardDto);
        return ResultUtil.success(page);
    }

    /**
     * 获取奖励详情
     */
    @GetMapping("/rewards/{id}")
    public ResponseResult getReward(@PathVariable Long id) {
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
    public ResponseResult addReward(@RequestBody TodoRewardItem reward) {
        Long userId = SecurityUserUtils.getUserId();
        reward.setUserId(userId);
        reward.setStatus(1);
        reward.setCreateTime(LocalDateTime.now());
        reward.setCreateBy(userId);
        
        boolean saved = todoRewardItemService.save(reward);
        return saved ? ResultUtil.success(reward) : ResultUtil.error("添加失败");
    }

    /**
     * 编辑奖励商品
     */
    @PutMapping("/rewards/{id}")
    public ResponseResult editReward(@PathVariable Long id, @RequestBody TodoRewardItem reward) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem existingReward = todoRewardItemService.getById(id);
        if (existingReward == null || !existingReward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        
        reward.setId(id);
        reward.setUserId(userId);
        reward.setUpdateTime(LocalDateTime.now());
        reward.setUpdateBy(userId);
        
        boolean updated = todoRewardItemService.updateById(reward);
        return updated ? ResultUtil.success(reward) : ResultUtil.error("更新失败");
    }

    /**
     * 删除奖励商品
     */
    @DeleteMapping("/rewards/{id}")
    public ResponseResult deleteReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem reward = todoRewardItemService.getById(id);
        if (reward == null || !reward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        
        boolean deleted = todoRewardItemService.removeById(id);
        return deleted ? ResultUtil.success("删除成功") : ResultUtil.error("删除失败");
    }

    /**
     * 兑换奖励
     */
    @PostMapping("/rewards/{id}/exchange")
    public ResponseResult exchangeReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        boolean exchanged = todoRewardItemService.exchangeReward(id, userId);
        return exchanged ? ResultUtil.success("兑换成功") : ResultUtil.error("兑换失败");
    }

    /**
     * 分页查询用户奖励列表
     */
    @GetMapping("/user-rewards")
    public ResponseResult listUserRewards(@RequestParam(defaultValue = "1") Integer pageNum,
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
    public ResponseResult useReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        boolean used = todoUserRewardService.useReward(id, userId);
        return used ? ResultUtil.success("使用成功") : ResultUtil.error("使用失败");
    }
}