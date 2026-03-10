package com.lww.littlenote.vo;

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

    private Integer year;

    private Integer month;

    private String monthLabel;

    private Integer taskTotal;

    private Integer completedTotal;

    private List<DateItem> dates;

    @Data
    public static class DateItem implements Serializable {

        private String date;

        private Integer day;

        private String weekDay;

        private Integer taskCount;

        private Integer completedCount;
    }
}
