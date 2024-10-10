package com.lww.security.config.authentication;

import com.lww.security.config.authentication.handler.AuthenticationFailHandler;
import com.lww.security.config.authentication.handler.AuthenticationSuccessHandler;
import com.lww.security.config.authentication.handler.CustomizeAccessNoPerissDeniedHandler;
import com.lww.security.config.authentication.handler.CustomizeAuthNoLoginEntryPoint;
import com.lww.security.service.LoginUserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author lww
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private LoginUserService userService;

    @Resource
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Resource
    private AuthenticationFailHandler loginFailureHandler;

    /**
     * 未登录时处理
     */
    @Resource
    private CustomizeAuthNoLoginEntryPoint customizeAuthNoLoginEntryPoint;

    /**
     * 无权限时处理
     */
    @Resource
    private CustomizeAccessNoPerissDeniedHandler accessNoPerissDeniedHandler;

    @Resource
    private SecurityPermit securityPermit;

    /**
     * 核心配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //  禁用basic明文验证
                .httpBasic(Customizer.withDefaults())
                // 根据需求选择是否禁用 CSRF
                .csrf(csrf -> csrf.disable())
                // 不使用 session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //  自定义登录处理
                .formLogin(fl ->
                        fl
                                .loginPage("/login.html")
                                .loginProcessingUrl("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                // 登录成功处理类
                                .successHandler(loginSuccessHandler)
                                // 登录失败处理类
                                .failureHandler(loginFailureHandler)
                                .permitAll())
                //  设置 处理鉴权失败、认证失败
                .exceptionHandling(
                        exceptions ->
                                exceptions
                                        // 未登录时处理
                                        .authenticationEntryPoint(customizeAuthNoLoginEntryPoint)
                                        // 无权限时处理
                                        .accessDeniedHandler(accessNoPerissDeniedHandler)
                )
                //  下面开始设置权限
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(AUTH_TEST_LIST).permitAll()
                                .requestMatchers(securityPermit.getUrls().toArray(new String[0])).permitAll()
                                //  允许任意请求被已登录用户访问，不检查Authority
                                .anyRequest().authenticated()
                )
                //  添加过滤器 jwt验证
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //可以加载iframe嵌套页面
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    /** 
     *
     * loadUserByUsername  通过用户输入username 程序员给定一个user
     * 来与用户输入的比对是否  账号密码正确
     * @return      
     * @author lww
     * @since 
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 将数据库的user对象 转换为 userDetails对象
        return username -> new SecurityUserDetails(userService.getUserByUserName(username));
    }

    /**
     * 配置 调取 login接口时的 登录 认证操作
     *
     * @author lww
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /** 
     *
     * 配置加密方式 如果不配置security会认为没有加密方式
     * @return      
     * @author lww
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 配置跨源访问(CORS)
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
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

