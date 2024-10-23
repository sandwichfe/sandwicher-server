package com.lww.auth.resource.controller;

import com.lww.auth.resource.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping(value = "/r1")
    @PreAuthorize("hasAuthority('p1')")//拥有p1权限方可访问此url
    public String r1(){
        //获取用户身份信息
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDTO.getUsername()+"访问资源1";
    }

    @GetMapping(value = "/r2")
    @PreAuthorize("hasAuthority('p2')")
    public String r2(){//通过Spring Security API获取当前登录用户
        UserDTO user =
                (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername() + "访问资源2";
    }

    @GetMapping(value = "/r3")
    public String r3(){//通过Spring Security API获取当前登录用户

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDTO user = (UserDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername() + "访问资源3";
    }

}