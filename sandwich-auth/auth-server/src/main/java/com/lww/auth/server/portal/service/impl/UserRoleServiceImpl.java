package com.lww.auth.server.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.portal.entity.UserRole;
import com.lww.auth.server.portal.mapper.UserRoleMapper;
import com.lww.auth.server.portal.service.UserRoleService;
import com.lww.auth.server.portal.vo.req.AssignRolesToUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return this.list(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // 先删除用户的所有角色
        this.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId));
        // 再添加新的角色
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());
            this.saveBatch(userRoles);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(AssignRolesToUserRequest request) {
        this.assignRolesToUser(request.getUserId(), request.getRoleIds());
    }
}
