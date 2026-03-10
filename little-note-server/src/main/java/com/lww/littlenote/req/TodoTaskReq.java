package com.lww.littlenote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


@Data
public class TodoTaskReq implements Serializable {

    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "任务内容")
    private String content;

    @Schema(description = "")
    private String taskType;

    @Schema(description = "")
    private String category;

    @Schema(description = "积分")
    private Integer points;

    @Schema(description = "目标次数")
    private Integer targetCount;

    @Schema(description = "每日仅一次")
    private Integer isDailyLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    public void setType(String type) {
        this.taskType = type;
    }
}
