package com.lww.auth.server.impl;

import com.lww.auth.server.dao.entity.AuthUser;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author haichen
 */
// @Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserDetails userDetails = new AuthUserDetails();
        // 模拟数据库查 用户
        AuthUser authUser = new AuthUser();
        authUser.setId(10000000000000L);
        authUser.setUsername("haichen");
        authUser.setPassword("{bcrypt}$2a$10$WM45iX/kMPgjpTRGKSSFIeQ49ukJV6dJYNyVpiZxUNLB3aXpGsSpe");
        authUser.setType(1);
        authUser.setStatus(1);
        if (Objects.nonNull(authUser)){
            BeanUtils.copyProperties(authUser, userDetails);
            //TOTO
            userDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("user:edit")));
        }


        return userDetails;
    }
}
