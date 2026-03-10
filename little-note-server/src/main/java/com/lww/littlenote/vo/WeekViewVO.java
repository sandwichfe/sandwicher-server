package com.lww.littlenote.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 周视图VO
 *
 * @author lww
 */
@Data
public class WeekViewVO implements Serializable {

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "周数")
    private Integer week;

    @Schema(description = "周标签")
    private String weekLabel;

    @Schema(description = "日期范围")
    private String dateRange;

    @Schema(description = "任务总数")
    private Integer taskTotal;

    @Schema(description = "已完成任务数")
    private Integer completedTotal;

    @Schema(description = "日期列表")
    private List<DayItem> days;

    @Data
    public static class DayItem implements Serializable {

        @Schema(description = "日期")
        private String date;

        @Schema(description = "星期")
        private String label;

        @Schema(description = "任务数")
        private Integer taskCount;

        @Schema(description = "已完成任务数")
        private Integer completedCount;

        @Schema(description = "任务列表")
        private List<TodoTaskVo> tasks;
    }
}
