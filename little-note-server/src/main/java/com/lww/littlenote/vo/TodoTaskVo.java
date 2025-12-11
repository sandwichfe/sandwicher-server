package com.lww.littlenote.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TodoTaskVo implements Serializable {
    private Long id;
    private String content;
    private String taskType;
    private String category;
    private Integer points;
    private String encouragement;
    private Integer targetCount;
    private Integer completedCount;
    private Long originalTaskId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate todoDate;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
