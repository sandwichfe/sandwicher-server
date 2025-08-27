package com.lww.auth.resources_server.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author lww
 * @since 2024/12/9
 */
@Configuration
@ComponentScan(basePackages = "com.lww.auth.resources_server.config")
@Slf4j
public class ResourcesServerConfig {

    @PostConstruct
    public void init() {
        // 在这里打印日志，表示配置类已被加载
        log.info("ResourcesServerConfig has been successfully loaded and initialized.");
    }

}
