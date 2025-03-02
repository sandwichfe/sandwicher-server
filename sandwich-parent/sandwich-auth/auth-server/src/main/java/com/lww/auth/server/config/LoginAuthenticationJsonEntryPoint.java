package com.lww.auth.server.config;

/**
 * 未授权响应实现
 * @author sandw
 */

import com.alibaba.fastjson.JSONObject;
import com.lww.common.web.response.ResultUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * 重定向至登录处理
 *
 * @author vains
 */
@Slf4j
public class LoginAuthenticationJsonEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(JSONObject.toJSONString(ResultUtil.error(HttpStatus.UNAUTHORIZED.value(),"未授权")));
        response.getWriter().flush();
    }
}


