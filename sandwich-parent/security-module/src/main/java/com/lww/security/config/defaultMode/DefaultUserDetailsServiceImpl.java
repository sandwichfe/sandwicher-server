package com.lww.security.config.defaultMode;

import com.lww.security.config.customMode.SecurityUserDetails;
import com.lww.security.entity.LoginUser;
import com.lww.security.service.LoginUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: security用户信息校验相关
 * @author lww
 * @since 2022/7/20 14:43
 */
@Component
public class DefaultUserDetailsServiceImpl implements UserDetailsService {

    /**
     * 从数据库中获取用户信息  返回一个userDetails对象
     * @author lww
     * @since 2022/7/20 14:57
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //正常从数据库查 用于对输入的用户密码验证
        LoginUser user  = new LoginUser();
        user.setUsername("lww");
        user.setPassword(new BCryptPasswordEncoder().encode("lww"));


        // 将数据库的user对象 转换为 userDetails对象
        return new SecurityUserDetails(user);
    }
}
