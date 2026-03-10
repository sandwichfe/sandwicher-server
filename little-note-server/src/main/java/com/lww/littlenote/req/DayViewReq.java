package com.lww.littlenote.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 日视图请求参数
 *
 * @author lww
 */
@Data
public class DayViewReq implements Serializable {

    /**
     * 查询日期，格式 yyyy-MM-dd
     */
    private String date;
}
