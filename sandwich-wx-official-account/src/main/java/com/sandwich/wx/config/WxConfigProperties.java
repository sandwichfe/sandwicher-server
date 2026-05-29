package com.sandwich.wx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置属性类
 * @author lww
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.config")
public class WxConfigProperties {
    
    /**
     * 微信公众平台配置的Token
     */
    private String token;
    
}