package com.lww.auth.resources_server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * @author sandw
 */
public class SecurityUserUtils {

    private SecurityUserUtils() {
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        Jwt jwt = getJwt();
        return jwt.getClaim("userId");
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        Jwt jwt = getJwt();
        return jwt.getClaim("userName");
    }

    /**
     * 获取用户权限列表
     */
    public static List<String> getAuthorities() {
        Jwt jwt = getJwt();
        return jwt.getClaim("authorities");
    }

    /**
     * 从 SecurityContext 中提取 JWT 对象
     */
    private static Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        throw new IllegalStateException("无法获取 JWT 信息");
    }
}
