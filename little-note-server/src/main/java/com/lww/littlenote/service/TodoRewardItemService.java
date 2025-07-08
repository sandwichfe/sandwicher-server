package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.dto.TodoRewardDto;
import com.lww.littlenote.entity.TodoRewardItem;

/**
 * <p>
 * 奖励商品表 服务类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
public interface TodoRewardItemService extends IService<TodoRewardItem> {

    /**
     * 分页查询奖励商品列表
     *
     * @param todoRewardDto 查询条件
     * @return 分页结果
     */
    Page<TodoRewardItem> listRewards(TodoRewardDto todoRewardDto);

    /**
     * 兑换奖励
     *
     * @param rewardId 奖励ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean exchangeReward(Long rewardId, Long userId);
}