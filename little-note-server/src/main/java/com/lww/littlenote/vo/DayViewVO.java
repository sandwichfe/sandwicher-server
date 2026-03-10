package com.lww.littlenote.vo;

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

    private String date;

    private String weekDay;

    private Integer taskTotal;

    private Integer completedTotal;

    private List<TimeSlot> schedule;

    @Data
    public static class TimeSlot implements Serializable {

        private String time;

        private List<TodoTaskVo> tasks;
    }
}
