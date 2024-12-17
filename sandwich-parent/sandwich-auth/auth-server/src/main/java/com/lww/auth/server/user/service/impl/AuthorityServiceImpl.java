package com.lww.auth.server.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user.entity.Authority;
import com.lww.auth.server.user.mapper.AuthorityMapper;
import com.lww.auth.server.user.service.AuthorityService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements AuthorityService {

}
