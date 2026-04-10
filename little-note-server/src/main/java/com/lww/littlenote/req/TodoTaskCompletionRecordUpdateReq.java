package com.lww.littlenote.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TodoTaskCompletionRecordUpdateReq implements Serializable {

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime completedAt;
}
