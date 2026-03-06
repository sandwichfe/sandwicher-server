package com.lww.littlenote.req;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


@Data
public class TodoTaskReq implements Serializable {
    private Long id;
    private String content;
    private String taskType;
    private String category;
    private Integer points;
    private String encouragement;
    private Integer targetCount;
    private Integer isDailyLimit;
    private LocalDate todoDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    public void setType(String type) {
        this.taskType = type;
    }
}
