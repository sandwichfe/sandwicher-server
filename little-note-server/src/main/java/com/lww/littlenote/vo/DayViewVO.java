package com.lww.littlenote.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 日视图VO
 *
 * @author lww
 */
@Data
public class DayViewVO implements Serializable {

    @Schema(description = "日期")
    private String date;

    @Schema(description = "星期")
    private String weekDay;

    @Schema(description = "任务总数")
    private Integer taskTotal;

    @Schema(description = "已完成任务数")
    private Integer completedTotal;

    @Schema(description = "日程")
    private List<TimeSlot> schedule;

    @Data
    public static class TimeSlot implements Serializable {

        @Schema(description = "时间")
        private String time;

        @Schema(description = "任务列表")
        private List<TodoTaskVo> tasks;
    }
}
