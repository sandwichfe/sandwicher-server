package com.lww.auth.dep.resources.server.config;

/**
 * todo
 *
 * @author lww
 * @since 2024/11/13
 */

import com.lww.auth.dep.resources.server.config.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class ResourceServerAutoConfiguration {

    @Resource
    private SecurityPermit securityPermit;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭csrf 要写在requestMatchers之前
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedOriginPatterns(List.of("*")); // 允许所有来源
                    config.addAllowedHeader("*"); // 允许所有头部
                    config.addAllowedMethod("*"); // 允许所有方法
                    return config;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        // 下边一行是放行接口的配置，被放行的接口上不能有权限注解，e.g. @PreAuthorize，否则无效
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(AUTH_TEST_LIST).permitAll()
                        .requestMatchers(securityPermit.getUrls().toArray(new String[0])).permitAll()
                        //  允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                                // 可在此处添加自定义解析设置
                                .jwt(Customizer.withDefaults())
                        // 添加未携带token和权限不足异常处理(已在第五篇文章中说过)
                       .accessDeniedHandler(SecurityUtils::exceptionHandler)
                       .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                );
        return http.build();
    }

    /**
     * swagger ui忽略
     */
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/webjars/**",
            // swagger-boostrap-ui
            "/doc.html",
    };


    /**
     * test忽略
     */
    private static final String[] AUTH_TEST_LIST = {
            "/css",
            "/html"
    };

}

