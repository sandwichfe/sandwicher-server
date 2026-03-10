package com.lww.littlenote.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TodoUserPointsVo implements Serializable {

    @Schema(description = "用户积分ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "总积分")
    private Integer totalPoints;

    @Schema(description = "当前积分")
    private Integer currentPoints;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
