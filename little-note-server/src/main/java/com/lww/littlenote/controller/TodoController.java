package com.lww.littlenote.controller;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.resources_server.utils.SecurityUserUtils;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.entity.TodoRewardItem;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.entity.TodoUserPoints;
import com.lww.littlenote.entity.TodoUserReward;
import com.lww.littlenote.req.TodoRewardItemReq;
import com.lww.littlenote.req.TodoRewardQueryReq;
import com.lww.littlenote.req.TodoTaskQueryReq;
import com.lww.littlenote.req.TodoTaskReq;
import com.lww.littlenote.service.TodoRewardItemService;
import com.lww.littlenote.service.TodoTaskService;
import com.lww.littlenote.service.TodoUserPointsService;
import com.lww.littlenote.service.TodoUserRewardService;
import com.lww.littlenote.vo.TaskStatsVO;
import com.lww.littlenote.vo.TodoRewardItemVo;
import com.lww.littlenote.vo.TodoTaskVo;
import com.lww.littlenote.vo.TodoUserPointsVo;
import com.lww.littlenote.vo.TodoUserRewardVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * Todo任务管理 前端控制器
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@ApiLittleNoteRestController
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
    public ResponseResult<IPage<TodoTaskVo>> listTasks(@RequestBody TodoTaskQueryReq todoTaskQueryReq) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskQueryReq.setUserId(userId);
        Page<TodoTask> page = todoTaskService.listTasks(todoTaskQueryReq);
        IPage<TodoTaskVo> pageVo = page.convert(item -> CustomBeanUtils.copyProperties(item, TodoTaskVo.class));
        return ResultUtil.success(pageVo);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/tasks/{id}")
    public ResponseResult<TodoTaskVo> getTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = todoTaskService.getById(id);
        if (task == null || !task.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        return ResultUtil.success(CustomBeanUtils.copyProperties(task, TodoTaskVo.class));
    }

    /**
     * 添加任务
     */
    @PostMapping("/tasks")
    public ResponseResult<TodoTaskVo> addTask(@RequestBody TodoTaskReq taskReq) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = new TodoTask();
        BeanUtils.copyProperties(taskReq, task);
        task.setUserId(userId);
        task.setCompletedCount(0);
        task.setCreateTime(LocalDateTime.now());
        task.setCreateBy(userId);
        
        if (task.getTargetCount() == null || task.getTargetCount() <= 0) {
            task.setTargetCount(1);
        }
        
        boolean saved = todoTaskService.save(task);
        if (saved) {
            return ResultUtil.success(CustomBeanUtils.copyProperties(task, TodoTaskVo.class));
        }
        return ResultUtil.error("添加失败");
    }

    /**
     * 编辑任务
     */
    @PutMapping("/tasks/{id}")
    public ResponseResult<TodoTaskVo> editTask(@PathVariable Long id, @RequestBody TodoTaskReq taskReq) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask existingTask = todoTaskService.getById(id);
        if (existingTask == null || !existingTask.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        
        TodoTask task = new TodoTask();
        BeanUtils.copyProperties(taskReq, task);
        task.setId(id);
        task.setUserId(userId);
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateBy(userId);
        
        boolean updated = todoTaskService.updateById(task);
        if (updated) {
            return ResultUtil.success(CustomBeanUtils.copyProperties(task, TodoTaskVo.class));
        }
        return ResultUtil.error("更新失败");
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
    public ResponseResult<TodoUserPointsVo> getUserPoints() {
        Long userId = SecurityUserUtils.getUserId();
        TodoUserPoints userPoints = todoUserPointsService.getUserPoints(userId);
        return ResultUtil.success(CustomBeanUtils.copyProperties(userPoints, TodoUserPointsVo.class));
    }

    /**
     * 分页查询奖励商品列表
     */
    @PostMapping("/rewards/list")
    public ResponseResult<IPage<TodoRewardItemVo>> listRewards(@RequestBody TodoRewardQueryReq todoRewardQueryReq) {
        Long userId = SecurityUserUtils.getUserId();
        todoRewardQueryReq.setUserId(userId);
        Page<TodoRewardItem> page = todoRewardItemService.listRewards(todoRewardQueryReq);
        IPage<TodoRewardItemVo> pageVo = page.convert(item -> CustomBeanUtils.copyProperties(item, TodoRewardItemVo.class));
        return ResultUtil.success(pageVo);
    }

    /**
     * 获取奖励详情
     */
    @GetMapping("/rewards/{id}")
    public ResponseResult<TodoRewardItemVo> getReward(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem reward = todoRewardItemService.getById(id);
        if (reward == null || !reward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        return ResultUtil.success(CustomBeanUtils.copyProperties(reward, TodoRewardItemVo.class));
    }

    /**
     * 添加奖励商品
     */
    @PostMapping("/rewards")
    public ResponseResult<Void> addReward(@RequestBody TodoRewardItemReq rewardReq) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem reward = new TodoRewardItem();
        BeanUtils.copyProperties(rewardReq, reward);
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
    public ResponseResult<Void> editReward(@PathVariable Long id, @RequestBody TodoRewardItemReq rewardReq) {
        Long userId = SecurityUserUtils.getUserId();
        TodoRewardItem existingReward = todoRewardItemService.getById(id);
        if (existingReward == null || !existingReward.getUserId().equals(userId)) {
            return ResultUtil.error("奖励不存在");
        }
        
        TodoRewardItem reward = new TodoRewardItem();
        BeanUtils.copyProperties(rewardReq, reward);
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
    public ResponseResult<IPage<TodoUserRewardVo>> listUserRewards(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String status) {
        Long userId = SecurityUserUtils.getUserId();
        Page<TodoUserReward> page = todoUserRewardService.listUserRewards(userId, pageNum, pageSize, status);
        IPage<TodoUserRewardVo> pageVo = page.convert(item -> CustomBeanUtils.copyProperties(item, TodoUserRewardVo.class));
        return ResultUtil.success(pageVo);
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