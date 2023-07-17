package com.lww.sandwich.service;

import com.lww.sandwich.entity.User;
import org.springframework.stereotype.Service;

/**
 * @description: 用户Service
 * @author lww
 * @since 2022/7/20 14:58
 */
public interface UserService {
    /** 
     * 根据用户名查询用户信息
     * @author lww
     * @since 2022/7/20 15:03
     * @param username
     * @return
     */
    User getUserByUserName(String username);
}
