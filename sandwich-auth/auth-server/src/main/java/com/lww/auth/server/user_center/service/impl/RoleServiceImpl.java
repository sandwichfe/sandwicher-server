package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.Role;
import com.lww.auth.server.user_center.mapper.RoleMapper;
import com.lww.auth.server.user_center.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
