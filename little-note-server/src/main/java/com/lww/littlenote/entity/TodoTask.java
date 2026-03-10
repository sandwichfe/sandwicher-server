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
     * 积分奖励
     */
    private Integer points;

    /**
     * 目标完成次数
     */
    private Integer targetCount;

    /**
     * 已完成次数
     */
    private Integer completedCount;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deadline;

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

    /**
     * 任务状态: 0-未完成, 1-已完成
     */
    private Integer status;

    /**
     * 是否每日仅可完成一次: 0-否, 1-是
     */
    private Integer isDailyLimit;

    /**
     * 上次完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastCompleteTime;
}
