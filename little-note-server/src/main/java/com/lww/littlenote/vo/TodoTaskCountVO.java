package com.lww.littlenote.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoTaskCountVO {

    @Schema(description = "待完成任务数")
    private Long pendingCount;

    @Schema(description = "已完成任务数")
    private Long completedCount;
}
