package com.lww.security.config;

import com.lww.security.config.authentication.handler.AuthenticationFailHandler;
import com.lww.security.config.authentication.handler.AuthenticationSuccessHandler;
import com.lww.security.config.authentication.handler.CustomizeAccessNoPerissDeniedHandler;
import com.lww.security.config.authentication.handler.CustomizeAuthNoLoginEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author lww
 * @since 1.0.0
 */
// 如今的写法
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Resource
    private AuthenticationFailHandler loginFailureHandler;

    /**未登录时处理*/
    @Resource
    private CustomizeAuthNoLoginEntryPoint customizeAuthNoLoginEntryPoint;

    /**无权限时处理*/
    @Resource
    private CustomizeAccessNoPerissDeniedHandler accessNoPerissDeniedHandler;

    @Value("${security.permit-urls:/default/value/**}")
    private String[] permitUrls;



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    // WebSecurityCustomizer是一个相似于Consumer的接口，函数承受一个WebSecurity类型的变量，无返回值
    // 此处运用lambda完成WebSecurityCustomizer接口，web变量的类型WebSecurity，箭头后面能够对其停止操作
    // 运用requestMatchers()替代antMatchers()
    return (web) -> web.ignoring().requestMatchers("/ignore1", "/ignore2");
    }


    /**
     * 核心配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //  禁用basic明文验证
                .httpBasic(Customizer.withDefaults())
                //  基于 token ，不需要 csrf
                //  禁用默认登录页
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
                                .defaultSuccessUrl("/")
                                .permitAll())
                //  禁用默认登出页
                //  基于 token ， 不需要 session
                //  设置 处理鉴权失败、认证失败
                // .exceptionHandling(
                //         exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint)
                //                 .accessDeniedHandler(jwtAccessDeniedHandler)
                // )
                //  下面开始设置权限
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(AUTH_TEST_LIST).permitAll()
                        .requestMatchers(permitUrls).permitAll()
                                //  允许任意请求被已登录用户访问，不检查Authority
                                .anyRequest().authenticated()
                );
                //  添加过滤器
                // .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(),7));
        //可以加载fram嵌套页面
        http.headers( headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService::loadUserByUsername;
    }



    /**
     * 调用loadUserByUserName获取userDetail信息，在AbstractUserDetailsAuthenticationProvider里执行用户状态检查
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //加密 方式
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
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
            "/v2/api-docs",
            "/webjars/**",
            // swagger-boostrap-ui
            "/doc.html",
            "/druid/**",
            "/user/registerUser"
    };


    /**
     * test忽略
     */
    private static final String[] AUTH_TEST_LIST = {
            "/css",
            "/html"
    };

}

