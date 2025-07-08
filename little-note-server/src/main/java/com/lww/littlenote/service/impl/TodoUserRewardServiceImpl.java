package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.entity.TodoUserReward;
import com.lww.littlenote.mapper.TodoUserRewardMapper;
import com.lww.littlenote.service.TodoUserRewardService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户奖励表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Service
public class TodoUserRewardServiceImpl extends ServiceImpl<TodoUserRewardMapper, TodoUserReward> implements TodoUserRewardService {

    @Override
    public Page<TodoUserReward> listUserRewards(Long userId, Integer pageNum, Integer pageSize, String status) {
        Page<TodoUserReward> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TodoUserReward> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper
                .eq(TodoUserReward::getUserId, userId)
                .eq(StringUtils.hasText(status), TodoUserReward::getStatus, status)
                .orderByDesc(TodoUserReward::getObtainedDate);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean useReward(Long userRewardId, Long userId) {
        TodoUserReward userReward = this.getById(userRewardId);
        if (userReward == null || !userReward.getUserId().equals(userId) || !"obtained".equals(userReward.getStatus())) {
            return false;
        }
        
        userReward.setStatus(1);
        userReward.setUsedDate(LocalDate.now());
        userReward.setUpdateTime(LocalDateTime.now());
        userReward.setUpdateBy(userId);
        
        return this.updateById(userReward);
    }
}