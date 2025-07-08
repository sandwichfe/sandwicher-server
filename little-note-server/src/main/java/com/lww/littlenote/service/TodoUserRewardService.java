package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.TodoUserReward;

/**
 * <p>
 * 用户奖励表 服务类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
public interface TodoUserRewardService extends IService<TodoUserReward> {

    /**
     * 分页查询用户奖励列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param status 状态
     * @return 分页结果
     */
    Page<TodoUserReward> listUserRewards(Long userId, Integer pageNum, Integer pageSize, String status);

    /**
     * 使用奖励
     *
     * @param userRewardId 用户奖励ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean useReward(Long userRewardId, Long userId);
}