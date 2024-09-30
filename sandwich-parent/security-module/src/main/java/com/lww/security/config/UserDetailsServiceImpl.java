package com.lww.security.config;

import com.lww.security.entity.LoginUser;
import com.lww.security.service.LoginUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description security用户信息校验相关
 * @author lww
 * @since 2022/7/20 14:43
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private LoginUserService userService;

    /**
     * 从数据库中获取用户信息  返回一个userDetails对象
     * @author lww
     * @since 2022/7/20 14:57
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser user  = userService.getUserByUserName(username);
        // 将数据库的user对象 转换为 userDetails对象
        return new SecurityUserDetails(user);
    }
}
