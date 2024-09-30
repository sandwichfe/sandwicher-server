package com.lww.security.config.authentication.handler;

import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.utils.ResponseOutUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *     用户未登录时的处理
 * @author lww
 * @since 2022/7/21 10:35
 */
@Component
public class CustomizeAuthNoLoginEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.UNAUTHORIZED, "未登录，请前去登录！"));
    }
}
