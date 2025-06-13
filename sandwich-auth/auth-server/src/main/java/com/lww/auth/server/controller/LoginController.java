package com.lww.auth.server.controller;

import com.lww.common.web.log.Loggable;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lww
 */
@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    @Loggable(module = "login", type = "login", description = "登录")
    public String login(Model model, HttpSession session) {
        // 如果发生错误 一般存在session里面 取出来显示
        Object attribute = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (attribute instanceof AuthenticationException exception) {
            model.addAttribute("error", exception.getMessage());
            log.error("登录失败", exception);
        }

        // 返回login.html
        return "login";
    }
}