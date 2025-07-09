package com.lww.littlenote.vo;

import lombok.Data;

/**
 * 任务统计值对象，用于封装任务相关的统计数据
 *
 * @author lww
 * @since 2025/7/9
 */
@Data
public class TaskStatsVO {

    /**
     * 总任务数
     */
    private Long totalTasks;

    /**
     * 已完成任务数
     */
    private Long completedTasks;

    /**
     * 待完成任务数
     */
    private Long pendingTasks;

    /**
     * 任务完成率(0.0到1.0之间)
     */
    private Double completionRate;

    /**
     * 构造函数，根据总任务数和已完成任务数创建统计对象
     * @param totalTasks 总任务数
     * @param completedTasks 已完成任务数
     */
    public TaskStatsVO(long totalTasks, long completedTasks) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = totalTasks - completedTasks;
        this.completionRate = totalTasks > 0 ? (double) completedTasks / totalTasks : 0.0;
    }
}

