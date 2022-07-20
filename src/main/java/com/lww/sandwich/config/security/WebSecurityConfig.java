package com.lww.sandwich.config.security;

import com.lww.sandwich.config.security.AuthenticationHandler.AuthenticationFailHandler;
import com.lww.sandwich.config.security.AuthenticationHandler.AuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * @description: security权限拦截相关配置类
 * @author lww
 * @since 2022/7/20 14:20
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Resource
    private AuthenticationFailHandler authenticationFailHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        registry.and()
                //表单登录  登录方式
                .formLogin()
                .permitAll()
                //// 登录成功处理类
                .successHandler(authenticationSuccessHandler)
                //// 登录失败处理类
                .failureHandler(authenticationFailHandler)
                .and()
                .authorizeRequests()
                // 任何请求
                .anyRequest()
                // 都需要身份验证
                .authenticated()
                .and()
                .csrf().disable()
                //使用jwt进行登录验证 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //加密 方式
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
