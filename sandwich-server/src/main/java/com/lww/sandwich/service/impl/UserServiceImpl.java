package com.lww.sandwich.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.web.exception.AppException;
import com.lww.sandwich.entity.User;
import com.lww.sandwich.mapper.UserMapper;
import com.lww.sandwich.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *  
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
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //if ("aaa".equals(username)) {
        //    return new User("aaa", passwordEncoder.encode("aaa"));
        //}
        // 查询此用户名
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername,username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 注册
     * @author lww
     * @since 2023/8/16 13:59
     * @param user user
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (!StringUtils.hasText(username)){
            throw new AppException("用户名不能为空");
        }
        if (!StringUtils.hasText(password)){
            throw new AppException("密码不能为空");
        }
        // 此用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        boolean existsUserName = userMapper.exists(wrapper);
        if (existsUserName){
            throw new AppException("用户名已存在");
        }
        User addUser = new User();
        addUser.setUsername(username);
        // addUser.setPassword(new BCryptPasswordEncoder().encode(password));
        userMapper.insert(addUser);
    }
}
