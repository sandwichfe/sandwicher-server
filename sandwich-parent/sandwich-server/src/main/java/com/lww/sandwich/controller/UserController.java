package com.lww.sandwich.controller;

import com.lww.response.ResponseResult;
import com.lww.sandwich.entity.User;
import com.lww.sandwich.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @description:
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
