package com.lww.littlenote.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 任务查询DTO
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Data
public class TodoTaskDto {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 任务分类: work/study/health
     */
    private String taskType;

    /**
     * 任务类别: global-全局任务, daily-每日待办
     */
    private String category;

    /**
     * 待办日期(仅每日待办使用)
     */
    private LocalDate todoDate;

    /**
     * 完成状态: pending-待完成, completed-已完成
     */
    private String status;
}