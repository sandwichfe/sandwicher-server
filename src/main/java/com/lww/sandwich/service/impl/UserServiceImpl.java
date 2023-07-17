package com.lww.sandwich.service.impl;

import com.lww.sandwich.entity.User;
import com.lww.sandwich.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description:
 * @author lww
 * @since 2022/7/20 14:59
 */
@Service
public class UserServiceImpl implements UserService {


    @Override
    public User getUserByUserName(String username) {
        if (!StringUtils.hasLength(username)) {
            return null;
        }
        // 模拟存在数据库里面的密码是加密过后的
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if ("lww".equals(username)) {
            return new User("lww", passwordEncoder.encode("123456"));
        }

        if ("aaa".equals(username)) {
            return new User("aaa", passwordEncoder.encode("aaa"));
        }
        return null;
    }
}
