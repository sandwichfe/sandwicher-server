package com.lww.littlenote.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 月视图请求参数
 *
 * @author lww
 */
@Data
public class MonthViewReq implements Serializable {

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份（1~12）
     */
    private Integer month;
}
