package com.lww.auth.server.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user.entity.RoleAuthority;
import com.lww.auth.server.user.mapper.RoleAuthorityMapper;
import com.lww.auth.server.user.service.RoleAuthorityService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单多对多关联表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Service
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements RoleAuthorityService {

}
