package com.lww.auth.server.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权客户端白名单配置
 * 从配置文件读取客户端信息
 *
 * @author lww
 * @since 2026/06/30
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth.client")
public class AuthClientProperties {

    /**
     * 客户端白名单列表
     */
    private List<ClientInfo> whitelist = new ArrayList<>();

    /**
     * 客户端信息
     */
    @Data
    public static class ClientInfo {
        /**
         * 客户端ID
         */
        private String clientId;

        /**
         * 允许的回调地址列表
         */
        private List<String> redirectUris = new ArrayList<>();
    }
}
