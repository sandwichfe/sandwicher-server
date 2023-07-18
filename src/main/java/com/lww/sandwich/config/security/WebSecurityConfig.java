package com.lww.sandwich.config.security;

import com.lww.sandwich.config.security.AuthenticationHandler.AuthenticationFailHandler;
import com.lww.sandwich.config.security.AuthenticationHandler.AuthenticationSuccessHandler;
import com.lww.sandwich.config.security.AuthenticationHandler.CustomizeAccessNoPerissDeniedHandler;
import com.lww.sandwich.config.security.AuthenticationHandler.CustomizeAuthNoLoginEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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

    /**未登录时处理*/
    @Resource
    private CustomizeAuthNoLoginEntryPoint customizeAuthNoLoginEntryPoint;

    /**无权限时处理*/
    @Resource
    private CustomizeAccessNoPerissDeniedHandler accessNoPerissDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        http.cors().and().csrf().disable();
        registry.and()
                // EntryPoint屏蔽security自带的重定向login页面   异常处理(权限拒绝、登录失效等)
                .exceptionHandling()
                // 未登录时处理
                .authenticationEntryPoint(customizeAuthNoLoginEntryPoint)
                // 没有权限时处理
                .accessDeniedHandler(accessNoPerissDeniedHandler)
                .and()
                //表单登录  登录方式
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                // 登录成功处理类
                .successHandler(authenticationSuccessHandler)
                // 登录失败处理类
                .failureHandler(authenticationFailHandler)
                .and()
                .authorizeRequests()
                // 任何请求
                .anyRequest()
                // 都需要身份验证
                .authenticated()
                .and()
                //使用jwt进行登录验证 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),7));
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //加密 方式
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore
        web.ignoring().antMatchers("/base/getRequestIp","/base/test", "/css", "/html")
                .antMatchers(AUTH_WHITELIST);
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
            "/formatConvert/**"
    };
}
