package com.lww.security.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author ChengJianSheng
 * @date 2019-03-03
 */
@EnableOAuth2Sso
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequest ->
                authorizeHttpRequest
                        .requestMatchers("/bootstrap/**").permitAll()
                        .requestMatchers("/oauth/token").permitAll()
                        .requestMatchers( "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/doc.html").permitAll()
                        //  允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated()
        )
                .logout().logoutSuccessUrl("http://localhost:8080/logout")
                .and()
                .csrf().disable();
        return http.build();
    }


}

