package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.TodoUserPoints;

/**
 * <p>
 * 用户积分表 服务类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
public interface TodoUserPointsService extends IService<TodoUserPoints> {

    /**
     * 获取用户积分信息
     *
     * @param userId 用户ID
     * @return 积分信息
     */
    TodoUserPoints getUserPoints(Long userId);

    /**
     * 增加积分
     *
     * @param userId 用户ID
     * @param points 积分数量
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @return 是否成功
     */
    boolean addPoints(Long userId, Integer points, String sourceType, Long sourceId, String description);

    /**
     * 扣除积分
     *
     * @param userId 用户ID
     * @param points 积分数量
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @return 是否成功
     */
    boolean deductPoints(Long userId, Integer points, String sourceType, Long sourceId, String description);
}