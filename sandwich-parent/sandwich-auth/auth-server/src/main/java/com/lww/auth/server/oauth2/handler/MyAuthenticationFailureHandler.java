package com.lww.auth.server.oauth2.handler;

import java.io.IOException;

import cn.hutool.http.HttpStatus;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Oauth2认证成功处理器
 * @author sandw
 */
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * MappingJackson2HttpMessageConverter 是 Spring 框架提供的一个 HTTP 消息转换器，用于将 HTTP 请求和响应的 JSON 数据与 Java 对象之间进行转换
     */
    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        ResponseResult result = ResultUtil.error(HttpStatus.HTTP_UNAUTHORIZED,error.getErrorCode());
        accessTokenHttpResponseConverter.write(result, null, httpResponse);
    }
}
