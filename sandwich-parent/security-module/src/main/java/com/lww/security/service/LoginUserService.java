package com.lww.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.security.entity.LoginUser;
import com.lww.common.web.response.ResponseResult;

/**
 *   用户Service
 * @author lww
 * @since 2022/7/20 14:58
 */
public interface LoginUserService extends IService<LoginUser> {
    /** 
     * 根据用户名查询用户信息
     * @author lww
     * @since 2022/7/20 15:03
     * @param username
     * @return
     */
    LoginUser getUserByUserName(String username);

    /**
     * 注册
     * @author lww
     * @since 2023/8/16 13:59
     */
    ResponseResult registerUser(LoginUser user);
}
