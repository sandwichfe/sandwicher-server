package com.lww.littlenote.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 月视图VO
 *
 * @author lww
 */
@Data
public class MonthViewVO implements Serializable {

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "月份")
    private Integer month;

    @Schema(description = "月份标签")
    private String monthLabel;

    @Schema(description = "任务总数")
    private Integer taskTotal;

    @Schema(description = "已完成任务数")
    private Integer completedTotal;

    @Schema(description = "日期列表")
    private List<DateItem> dates;

    @Data
    public static class DateItem implements Serializable {

        @Schema(description = "日期")
        private String date;

        @Schema(description = "日期")
        private Integer day;

        @Schema(description = "星期")
        private String weekDay;

        @Schema(description = "任务数")
        private Integer taskCount;

        @Schema(description = "已完成任务数")
        private Integer completedCount;
    }
}
