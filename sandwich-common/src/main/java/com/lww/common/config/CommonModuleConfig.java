package com.lww.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * starter自动注入配置类
 *
 * @author lww
 */
@Configuration
@ComponentScan("com.lww.common")
@Import(CorsConfig.class)
public class CommonModuleConfig {
}
