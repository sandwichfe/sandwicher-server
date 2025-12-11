package com.lww.littlenote.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TodoUserRewardVo implements Serializable {
    private Long id;
    private Long rewardId;
    private String rewardName;
    private Integer pointsCost;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate obtainedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate usedDate;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
