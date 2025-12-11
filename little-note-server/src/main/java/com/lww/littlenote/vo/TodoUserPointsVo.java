package com.lww.littlenote.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TodoUserPointsVo implements Serializable {
    private Long id;
    private Long userId;
    private Integer totalPoints;
    private Integer currentPoints;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
