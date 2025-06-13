package com.lww.common.config;

import com.lww.common.dict.DictInitComponent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
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
@MapperScan("com.lww.common.web.log.mapper")
@Import(CorsConfig.class)
public class CommonModuleConfig {

    /**
     * 字典转化 Component
     *
     * @return DictInitComponent
     * @author lww
     */
    @Bean
    public DictInitComponent dicInitComponent() {
        return new DictInitComponent();
    }

}
