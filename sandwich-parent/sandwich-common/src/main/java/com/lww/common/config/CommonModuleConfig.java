package com.lww.common.config;

import com.lww.common.dict.DictInitComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * starter自动注入配置类
 *
 * @author lww
 * @since 1.4.0
 */
@Configuration
@ComponentScan("com.lww.common")
@Import(CorsConfig.class)
public class CommonModuleConfig {

    /**
     * 字典转化 Component
     *
     * @return
     * @author lww
     * @since
     */
    @Bean
    public DictInitComponent dicInitComponent() {
        DictInitComponent dicInitComponent = new DictInitComponent();
        return dicInitComponent;
    }

}
