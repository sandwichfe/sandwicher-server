package com.lww.littlenote.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lww
 *   swagger配置
 * @since 2022/3/7 15:29
 */
@Configuration
public class SwaggerConfig {

    /**
     * 创建了一个api接口的分组
     * 除了配置文件方式创建分组，也可以通过注册bean创建分组
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                // 分组名称
                .group("littlenote文档")
                // 接口请求路径规则
                .pathsToMatch("/**")
                .build();
    }



    /**
     * 配置基本信息
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 标题
                        .title("littlenote文档")
                        // 描述Api接口文档的基本信息
                        .description("平台管理服务api")
                        // 版本
                        .version("v1.0.0")
                        // 设置OpenAPI文档的联系信息，姓名，邮箱。
                        .contact(new Contact().name("lww").email("lww.com"))
                        // 设置OpenAPI文档的许可证信息，包括许可证名称为"Apache 2.0"，许可证URL为"http://springdoc.org"。
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }


}
