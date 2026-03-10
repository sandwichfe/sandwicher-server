package com.lww.littlenote.req;

import lombok.Data;

import java.time.LocalDate;
import java.io.Serializable;

/**
 * <p>
 * 任务查询Req
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Data
public class TodoTaskQueryReq implements Serializable {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 任务分类: work/study/health
     */
    private String taskType;


    /**
     * 完成状态: 0-待完成, 1-已完成
     */
    private String status;
}
