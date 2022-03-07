package com.lww.sandwicherserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author lww
 * @description: swagger3配置
 * @date 2022/3/7 15:29
 */
@Configuration
public class Swagger3Config {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).apiInfo(
                new ApiInfoBuilder()
                        .contact(new Contact("lww", "", "1344382941@qq.com"))
                        .title("Swagger3测试项目")
                        .build()
        );
    }
}
