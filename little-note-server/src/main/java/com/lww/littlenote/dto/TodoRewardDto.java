package com.lww.littlenote.dto;

import com.lww.common.web.vo.PageVo;
import lombok.Data;

/**
 * <p>
 * 奖励查询DTO
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Data
public class TodoRewardDto extends PageVo {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 状态: 1-可用, 0-禁用
     */
    private Integer status;

    /**
     * 最小积分
     */
    private Integer minPoints;

    /**
     * 最大积分
     */
    private Integer maxPoints;
}