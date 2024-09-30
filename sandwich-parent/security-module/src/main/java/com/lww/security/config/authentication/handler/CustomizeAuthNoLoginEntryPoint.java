package com.lww.security.config.authentication.handler;

import com.lww.response.ResponseCode;
import com.lww.response.ResultUtil;
import com.lww.utils.ResponseOutUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
