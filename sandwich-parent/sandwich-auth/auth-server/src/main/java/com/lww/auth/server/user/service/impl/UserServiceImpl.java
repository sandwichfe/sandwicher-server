package com.lww.auth.server.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user.entity.User;
import com.lww.auth.server.user.mapper.UserMapper;
import com.lww.auth.server.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础用户信息表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
