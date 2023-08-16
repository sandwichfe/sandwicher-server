package com.lww.sandwich.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.sandwich.entity.User;
import com.lww.sandwich.mapper.UserMapper;
import com.lww.sandwich.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @description:
 * @author lww
 * @since 2022/7/20 14:59
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserByUserName(String username) {
        if (!StringUtils.hasLength(username)) {
            return null;
        }
        // 模拟存在数据库里面的密码是加密过后的
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //if ("aaa".equals(username)) {
        //    return new User("aaa", passwordEncoder.encode("aaa"));
        //}
        // 查询此用户名
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        return user;
    }
}
