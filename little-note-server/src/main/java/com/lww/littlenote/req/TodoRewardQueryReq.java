package com.lww.littlenote.req;

import com.lww.common.web.vo.PageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * <p>
 * 奖励查询Req
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TodoRewardQueryReq extends PageVo implements Serializable {

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
