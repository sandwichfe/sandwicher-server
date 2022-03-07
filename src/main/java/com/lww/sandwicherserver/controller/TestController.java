package com.lww.sandwicherserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lww
 * @description: 测试Controller
 * @date 2022/3/7 13:46
 */
@RestController
@RequestMapping("/aa")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello sandwich";
    }
}
