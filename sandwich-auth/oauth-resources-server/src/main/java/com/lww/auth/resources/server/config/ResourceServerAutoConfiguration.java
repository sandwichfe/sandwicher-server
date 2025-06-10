package com.lww.auth.resources.server.config;

/**
 * todo
 *
 * @author lww
 * @since 2024/11/13
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerAutoConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        // 下边一行是放行接口的配置，被放行的接口上不能有权限注解，e.g. @PreAuthorize，否则无效
                        // .requestMatchers("/test02").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                                // 可在此处添加自定义解析设置
                                .jwt(Customizer.withDefaults())
                        // 添加未携带token和权限不足异常处理(已在第五篇文章中说过)
//                        .accessDeniedHandler(SecurityUtils::exceptionHandler)
//                        .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                );
        return http.build();
    }
}

