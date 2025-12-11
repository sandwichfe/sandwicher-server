package com.lww.littlenote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lww.common.web.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 统一任务表
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Data
@TableName("todo_task")
public class TodoTask extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 任务分类: work/study/health
     */
    private String taskType;

    /**
     * 任务类别: global-全局任务, daily-每日待办
     */
    private String category;

    /**
     * 积分奖励
     */
    private Integer points;

    /**
     * 鼓励语
     */
    private String encouragement;

    /**
     * 目标完成次数
     */
    private Integer targetCount;

    /**
     * 已完成次数
     */
    private Integer completedCount;

    /**
     * 原始任务ID(从全局任务复制到每日待办时)
     */
    private Long originalTaskId;

    /**
     * 待办日期(仅每日待办使用)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todoDate;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;
}