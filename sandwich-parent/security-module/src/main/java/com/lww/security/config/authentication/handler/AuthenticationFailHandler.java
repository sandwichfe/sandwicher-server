package com.lww.security.config.authentication.handler;

import com.lww.response.ResponseCode;
import com.lww.response.ResultUtil;
import com.lww.utils.ResponseOutUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *   登录失败处理
 * @author lww
 * @since 2022/7/20 16:54
 */
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 默认情况下，不管你是用户名不存在，密码错误，SS 都会报出 Bad credentials 异常信息
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.UNAUTHORIZED,"用户名或密码错误"));
        } else if (e instanceof DisabledException) {
            ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.UNAUTHORIZED,"账户被禁用，请联系管理员"));
        } else {
            ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.INTERNAL_SERVER_ERROR,"登录失败，其他内部错误"));
        }
    }
}
