package com.lww.sandwich.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.sandwich.entity.User;
import com.lww.sandwich.entity.View;
import com.lww.response.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * @description 用户Service
 * @author lww
 * @since 2022/7/20 14:58
 */
public interface UserService extends IService<User> {
    /** 
     * 根据用户名查询用户信息
     * @author lww
     * @since 2022/7/20 15:03
     * @param username
     * @return
     */
    User getUserByUserName(String username);

    /**
     * 注册
     * @author lww
     * @since 2023/8/16 13:59
     */
    ResponseResult registerUser(User user);
}
