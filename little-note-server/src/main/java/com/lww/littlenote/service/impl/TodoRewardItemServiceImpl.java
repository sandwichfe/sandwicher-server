package com.lww.littlenote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.entity.TodoRewardItem;
import com.lww.littlenote.entity.TodoUserPoints;
import com.lww.littlenote.entity.TodoUserReward;
import com.lww.littlenote.mapper.TodoRewardItemMapper;
import com.lww.littlenote.req.TodoRewardQueryReq;
import com.lww.littlenote.service.TodoRewardItemService;
import com.lww.littlenote.service.TodoUserPointsService;
import com.lww.littlenote.service.TodoUserRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 奖励商品表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Service
public class TodoRewardItemServiceImpl extends ServiceImpl<TodoRewardItemMapper, TodoRewardItem> implements TodoRewardItemService {

    @Autowired
    private TodoUserPointsService todoUserPointsService;
    
    @Autowired
    private TodoUserRewardService todoUserRewardService;

    @Override
    public Page<TodoRewardItem> listRewards(TodoRewardQueryReq todoRewardQueryReq) {
        Page<TodoRewardItem> page = new Page<>(todoRewardQueryReq.getPageNum(), todoRewardQueryReq.getPageSize());
        LambdaQueryWrapper<TodoRewardItem> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(TodoRewardItem::getUserId, todoRewardQueryReq.getUserId())
                .eq(ObjectUtil.isNotNull(todoRewardQueryReq.getStatus()), TodoRewardItem::getStatus, todoRewardQueryReq.getStatus())
                .ge(todoRewardQueryReq.getMinPoints() != null, TodoRewardItem::getPoints, todoRewardQueryReq.getMinPoints())
                .le(todoRewardQueryReq.getMaxPoints() != null, TodoRewardItem::getPoints, todoRewardQueryReq.getMaxPoints())
                .orderByAsc(TodoRewardItem::getPoints);
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exchangeReward(Long rewardId, Long userId) {
        TodoRewardItem reward = this.getById(rewardId);
        if (reward == null || !reward.getUserId().equals(userId) || reward.getStatus()!=1) {
            return false;
        }
        
        // 检查用户积分是否足够
        TodoUserPoints userPoints = todoUserPointsService.getUserPoints(userId);
        if (userPoints.getAvailablePoints() < reward.getPoints()) {
            return false;
        }
        
        // 扣除积分
        boolean deducted = todoUserPointsService.deductPoints(userId, reward.getPoints(),
                "reward_exchange", rewardId, "兑换奖励：" + reward.getName());
        
        if (deducted) {
            // 创建用户奖励记录
            TodoUserReward userReward = new TodoUserReward();
            userReward.setRewardId(rewardId);
            userReward.setRewardName(reward.getName());
            userReward.setPointsCost(reward.getPoints());
            userReward.setUserId(userId);
            userReward.setObtainedDate(LocalDate.now());
            userReward.setCreateTime(LocalDateTime.now());
            userReward.setCreateBy(userId);
            
            return todoUserRewardService.save(userReward);
        }
        
        return false;
    }
}