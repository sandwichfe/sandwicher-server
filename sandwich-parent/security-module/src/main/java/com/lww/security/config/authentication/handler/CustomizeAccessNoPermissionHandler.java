package com.lww.security.config.authentication.handler;

import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.utils.ResponseOutUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *   无权限接口处理
 * @author lww
 * @since 2022/7/21 10:46
 */
@Component
public class CustomizeAccessNoPermissionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.UNAUTHORIZED, "暂无权限！"));
    }
}
