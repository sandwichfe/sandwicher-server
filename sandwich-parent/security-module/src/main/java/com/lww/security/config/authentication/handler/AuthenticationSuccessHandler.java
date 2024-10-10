package com.lww.security.config.authentication.handler;

import com.lww.common.utils.ResponseOutUtil;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResultUtil;
import com.lww.security.config.authentication.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *   登录成功的处理类
 * @author lww
 * @since 2022/7/20 16:41
 */
@Slf4j
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
        String token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                //主题 放入用户名
                .setSubject(username)
                //自定义属性 放入用户拥有请求权限
                .claim(SecurityConstant.AUTHORITIES, authorities)
                //失效时间   7天
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 60 * 60 * 24 * 1000))
                //签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY).compact();
        // 响应
        ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.SUCCESS, "登录成功",token));
    }
}
