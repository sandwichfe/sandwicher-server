package com.lww.auth.server.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user.entity.User;

/**
 * <p>
 * 基础用户信息表 服务类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUserName(String username);

    /**
     * 注册
     * @author lww
     * @since 2023/8/16 13:59
     * @param user 用户信息
     */
    void registerUser(User user);
}
