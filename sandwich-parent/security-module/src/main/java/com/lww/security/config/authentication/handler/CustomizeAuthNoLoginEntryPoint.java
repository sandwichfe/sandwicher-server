<<<<<<<< HEAD:sandwich-parent/security-module/src/main/java/com/lww/security/config/customMode/AuthenticationHandler/CustomizeAuthNoLoginEntryPoint.java
package com.lww.security.config.customMode.AuthenticationHandler;
========
package com.lww.security.config.authentication.handler;
>>>>>>>> 42d49d6 (security完善):sandwich-parent/security-module/src/main/java/com/lww/security/config/authentication/handler/CustomizeAuthNoLoginEntryPoint.java

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
 * @description:   用户未登录时的处理
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
