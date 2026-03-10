package com.lww.littlenote.vo;

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

    private Integer year;

    private Integer week;

    private String weekLabel;

    private String dateRange;

    private Integer taskTotal;

    private Integer completedTotal;

    private List<DayItem> days;

    @Data
    public static class DayItem implements Serializable {

        private String date;

        private String label;

        private Integer taskCount;

        private Integer completedCount;

        private List<TodoTaskVo> tasks;
    }
}
