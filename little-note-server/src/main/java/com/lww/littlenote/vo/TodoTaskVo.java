package com.lww.littlenote.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lww
 */
@Data
public class TodoTaskVo implements Serializable {

    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "任务内容")
    private String content;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "任务分类")
    private String category;

    @Schema(description = "任务积分")
    private Integer points;

    @Schema(description = "任务目标次数")
    private Integer targetCount;

    @Schema(description = "已完成次数")
    private Integer completedCount;

    @Schema(description = "任务状态")
    private Integer status;

    @Schema(description = "是否开启每日限制")
    private Integer isDailyLimit;

    @Schema(description = "原始任务ID")
    private Long originalTaskId;

    @Schema(description = "任务日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todoDate;

    @Schema(description = "任务截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deadline;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "最后完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastCompleteTime;
}
