package com.lww.security.config.defaultMode;

import com.lww.security.config.customMode.AuthenticationHandler.AuthenticationFailHandler;
import com.lww.security.config.customMode.AuthenticationHandler.AuthenticationSuccessHandler;
import com.lww.security.config.customMode.AuthenticationHandler.CustomizeAccessNoPerissDeniedHandler;
import com.lww.security.config.customMode.AuthenticationHandler.CustomizeAuthNoLoginEntryPoint;
import com.lww.security.config.customMode.JWTAuthenticationFilter;
import com.lww.security.config.customMode.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.annotation.Resource;

/**
 * @description: security权限拦截相关配置类
 * @author lww
 * @since 2022/7/20 14:20
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@ConditionalOnProperty(name = "security.default-mode", havingValue = "true", matchIfMissing = true)
public class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Lazy
    @Resource
    private DefaultUserDetailsServiceImpl defaultUserDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(defaultUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    //获取一个密码编码器对象
    @Bean
    public PasswordEncoder passwordEncoder(){
        //密码为明文方式
        return new BCryptPasswordEncoder();
    }

    //配置安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/*/**").authenticated()//访问/r开始的请求需要认证通过
                .anyRequest().permitAll()//其它请求全部放行
                .and()
                .formLogin().successForwardUrl("/login-success");//登录成功跳转到/login-success
        http.logout().logoutUrl("/logout");//退出地址
    }


}
