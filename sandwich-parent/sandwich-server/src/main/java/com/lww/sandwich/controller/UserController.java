package com.lww.sandwich.controller;

import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.entity.User;
import com.lww.sandwich.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lww
 * @since 2022/7/21 13:51
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/registerUser")
    public ResponseResult<Void> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResultUtil.response(ResponseCode.SUCCESS, "注册成功！");
    }

}
