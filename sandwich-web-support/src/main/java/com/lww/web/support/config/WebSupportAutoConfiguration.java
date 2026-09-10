package com.lww.web.support.config;

import com.lww.web.support.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * Web 通用能力自动配置。
 *
 * @author lww
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebSupportAutoConfiguration {

    /**
     * 注册默认的全局跨域配置，业务应用可声明同类型 Bean 覆盖。
     *
     * @return 跨域配置
     */
    @Bean
    @ConditionalOnMissingBean(CorsConfig.class)
    public CorsConfig corsConfig() {
        return new CorsConfig();
    }

    /**
     * 注册 favicon 请求处理配置。
     *
     * @return favicon 配置
     */
    @Bean
    @ConditionalOnMissingBean(FaviconConfiguration.class)
    public FaviconConfiguration faviconConfiguration() {
        return new FaviconConfiguration();
    }

    /**
     * 注册默认全局异常处理器，业务应用可声明同类型 Bean 覆盖。
     *
     * @return 全局异常处理器
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
