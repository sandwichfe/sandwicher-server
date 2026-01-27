package com.lww.littlenote.req;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

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

    public void setType(String type) {
        this.taskType = type;
    }
}
