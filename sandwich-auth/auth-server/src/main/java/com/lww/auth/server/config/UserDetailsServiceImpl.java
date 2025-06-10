package com.lww.auth.server.config;


import com.lww.auth.server.user.entity.User;
import com.lww.auth.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * security用户信息校验相关
 * 替代 @Bean public UserDetailsService()
 * @author lww
 * @since 2022/7/20 14:43
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private  final UserService userService;

    /**
     * 从数据库中获取用户信息  返回一个userDetails对象
     * @author lww
     * @since 2022/7/20 14:57
     * @param username 用户名
     * @return userDetails对象
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user  = userService.getUserByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 将数据库的user对象 转换为 userDetails对象
        return new SecurityUserDetails(user);
    }
}
