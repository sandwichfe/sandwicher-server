package com.lww.littlenote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.littlenote.entity.TodoPointsLog;
import com.lww.littlenote.entity.TodoUserPoints;
import com.lww.littlenote.mapper.TodoUserPointsMapper;
import com.lww.littlenote.service.TodoUserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户积分表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Service
public class TodoUserPointsServiceImpl extends ServiceImpl<TodoUserPointsMapper, TodoUserPoints> implements TodoUserPointsService {

    @Autowired
    private com.lww.littlenote.mapper.TodoPointsLogMapper todoPointsLogMapper;

    @Override
    public TodoUserPoints getUserPoints(Long userId) {
        LambdaQueryWrapper<TodoUserPoints> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TodoUserPoints::getUserId, userId);
        TodoUserPoints userPoints = this.getOne(queryWrapper);
        
        if (userPoints == null) {
            // 初始化用户积分记录
            userPoints = new TodoUserPoints();
            userPoints.setUserId(userId);
            userPoints.setTotalPoints(0);
            userPoints.setAvailablePoints(0);
            userPoints.setUsedPoints(0);
            userPoints.setCreateTime(LocalDateTime.now());
            userPoints.setCreateBy(userId);
            this.save(userPoints);
        }
        
        return userPoints;
    }

    @Override
    @Transactional
    public boolean addPoints(Long userId, Integer points, String sourceType, Long sourceId, String description) {
        if (points <= 0) {
            return false;
        }
        
        TodoUserPoints userPoints = getUserPoints(userId);
        userPoints.setTotalPoints(userPoints.getTotalPoints() + points);
        userPoints.setAvailablePoints(userPoints.getAvailablePoints() + points);
        userPoints.setUpdateTime(LocalDateTime.now());
        userPoints.setUpdateBy(userId);
        
        boolean updated = this.updateById(userPoints);
        
        if (updated) {
            // 记录积分变动日志
            TodoPointsLog log = new TodoPointsLog();
            log.setUserId(userId);
            log.setChangeType("add");
            log.setPoints(points);
            log.setSourceType(sourceType);
            log.setSourceId(sourceId);
            log.setDescription(description);
            log.setCreateTime(LocalDateTime.now());
            log.setCreateBy(userId);
            todoPointsLogMapper.insert(log);
        }
        
        return updated;
    }

    @Override
    @Transactional
    public boolean deductPoints(Long userId, Integer points, String sourceType, Long sourceId, String description) {
        if (points <= 0) {
            return false;
        }
        
        TodoUserPoints userPoints = getUserPoints(userId);
        
        // 检查可用积分是否足够
        if (userPoints.getAvailablePoints() < points) {
            return false;
        }
        
        userPoints.setAvailablePoints(userPoints.getAvailablePoints() - points);
        userPoints.setUsedPoints(userPoints.getUsedPoints() + points);
        userPoints.setUpdateTime(LocalDateTime.now());
        userPoints.setUpdateBy(userId);
        
        boolean updated = this.updateById(userPoints);
        
        if (updated) {
            // 记录积分变动日志
            TodoPointsLog log = new TodoPointsLog();
            log.setUserId(userId);
            log.setChangeType("deduct");
            log.setPoints(points);
            log.setSourceType(sourceType);
            log.setSourceId(sourceId);
            log.setDescription(description);
            log.setCreateTime(LocalDateTime.now());
            log.setCreateBy(userId);
            todoPointsLogMapper.insert(log);
        }
        
        return updated;
    }
}