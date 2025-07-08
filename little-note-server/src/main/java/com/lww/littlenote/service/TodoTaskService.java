package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.dto.TodoTaskDto;
import com.lww.littlenote.entity.TodoTask;

/**
 * <p>
 * 统一任务表 服务类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
public interface TodoTaskService extends IService<TodoTask> {

    /**
     * 分页查询任务列表
     *
     * @param todoTaskDto 查询条件
     * @return 分页结果
     */
    Page<TodoTask> listTasks(TodoTaskDto todoTaskDto);

    /**
     * 完成任务一次
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean completeTaskOnce(Long taskId, Long userId);

    /**
     * 复制任务到每日待办
     *
     * @param originalTaskId 原始任务ID
     * @param userId 用户ID
     * @return 新任务ID
     */
    Long copyToDaily(Long originalTaskId, Long userId);

    /**
     * 获取用户任务统计
     *
     * @param userId 用户ID
     * @param category 任务类别
     * @return 统计信息
     */
    Object getTaskStats(Long userId, String category);
}