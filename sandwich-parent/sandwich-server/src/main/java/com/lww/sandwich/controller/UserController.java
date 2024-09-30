package com.lww.sandwich.controller;

import com.lww.common.web.response.ResponseResult;
import com.lww.sandwich.entity.User;
import com.lww.sandwich.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  
 * @author lww
 * @since 2022/7/21 13:51
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/registerUser")
    public ResponseResult registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

}
