package com.lww.sandwich.config.security.AuthenticationHandler;

import com.alibaba.fastjson.JSON;
import com.lww.sandwich.response.ResponseCode;
import com.lww.sandwich.response.ResultUtil;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description: 登录成功的处理类
 * @author lww
 * @since 2022/7/20 16:41
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        // 授权相关
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            list.add(authority.getAuthority());
        }
        // 登录成功生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        //  token 需要保存至服务器一份，实现方式：redis or jwt
        // 响应
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        //输出结果
        response.getWriter().write(JSON.toJSONString(ResultUtil.response(ResponseCode.SUCCESS,"登录成功！",token)));
    }
}
