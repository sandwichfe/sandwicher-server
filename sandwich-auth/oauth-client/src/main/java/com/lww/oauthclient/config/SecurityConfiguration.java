package com.lww.oauthclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth->auth
                .anyRequest().authenticated()
        );
        // 开启oauth2Login
        http.oauth2Login(Customizer.withDefaults());
        // 关闭csrf保护 注销操作时 不会显示注销页面  而是直接注销
        http.csrf(csrf->csrf.disable());
        // 自定义登出配置 客户端登出后 去调用授权服务的登出接口
        http.logout(logout->logout.logoutSuccessUrl("http://oauth.local.server:9000/logout"));
        return http.build();
    }
}