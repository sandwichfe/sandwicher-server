<<<<<<<< HEAD:sandwich-parent/security-module/src/main/java/com/lww/security/config/customMode/AuthenticationHandler/AuthenticationSuccessHandler.java
package com.lww.security.config.customMode.AuthenticationHandler;
========
package com.lww.security.config.authentication.handler;
>>>>>>>> 42d49d6 (security完善):sandwich-parent/security-module/src/main/java/com/lww/security/config/authentication/handler/AuthenticationSuccessHandler.java

import com.lww.response.ResponseCode;
import com.lww.response.ResultUtil;
import com.lww.security.config.customMode.SecurityConstant;
import com.lww.utils.ResponseOutUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.Date;
import java.util.List;

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