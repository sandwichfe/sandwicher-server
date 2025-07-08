package com.lww.littlenote.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户奖励表
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Data
@TableName("todo_user_reward")
public class TodoUserReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 奖励商品ID
     */
    private Long rewardId;

    /**
     * 奖励名称
     */
    private String rewardName;

    /**
     * 消耗积分
     */
    private Integer pointsCost;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 获得日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate obtainedDate;

    /**
     * 使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate usedDate;

    /**
     * 状态: 0-未使用, 1-已使用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;
}