package com.lww.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lww
 * @since 1.4.0
 */
@Configuration
@ComponentScan("com.lww.security")
@MapperScan("com.lww.security.mapper")
public class SecurityBeanConfig {


}
