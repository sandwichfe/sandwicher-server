package com.lww.config;

import com.lww.dict.DicInitComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * starter自动注入配置类
 * @author lww
 * @since 1.4.0
 */
@Configuration
public class CommonModuleConfig {

    /** 
     * 字典转化 Component
     *
     * @return      
     * @author lww
     * @since 
     */
    @Bean
    public DicInitComponent dicInitComponent() {
        DicInitComponent dicInitComponent = new DicInitComponent();
        return dicInitComponent;
    }

}
