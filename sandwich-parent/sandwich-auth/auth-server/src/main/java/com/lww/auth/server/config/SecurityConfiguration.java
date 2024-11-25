package com.lww.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
class SecurityConfiguration {

    // OAuth2AuthorizationServer配置
    @Bean
    public SecurityFilterChain serverFilterChain(HttpSecurity http) throws Exception {
        //授权服务器的安全交给security的过滤器处理
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 自定义配置
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权页
                .authorizationEndpoint(auth -> auth.consentPage("/consent"))
                // 开启oidc
                .oidc(Customizer.withDefaults());
        // 未认证的请求异常处理（/Login）    指向到login地址
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
        return http.build();
    }

    // security配置
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        http.formLogin(form -> form
                // 自定义登录页面
                .loginPage("/login")
                // 处理登录请求接口
                .loginProcessingUrl("/login").permitAll());
        return http.build();
    }
}