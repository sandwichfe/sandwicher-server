<<<<<<<< HEAD:sandwich-parent/security-module/src/main/java/com/lww/security/config/customMode/AuthenticationHandler/CustomizeAccessNoPerissDeniedHandler.java
package com.lww.security.config.customMode.AuthenticationHandler;
========
package com.lww.security.config.authentication.handler;
>>>>>>>> 42d49d6 (security完善):sandwich-parent/security-module/src/main/java/com/lww/security/config/authentication/handler/CustomizeAccessNoPerissDeniedHandler.java

import com.lww.response.ResponseCode;
import com.lww.response.ResultUtil;
import com.lww.utils.ResponseOutUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 无权限接口处理
 * @author lww
 * @since 2022/7/21 10:46
 */
@Component
public class CustomizeAccessNoPerissDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.UNAUTHORIZED, "暂无权限！"));
    }
}
