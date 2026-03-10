package com.lww.littlenote.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 周视图请求参数
 *
 * @author lww
 */
@Data
public class WeekViewReq implements Serializable {

    /**
     * 年份
     */
    private Integer year;

    /**
     * ISO周数（1~53）
     */
    private Integer week;
}
