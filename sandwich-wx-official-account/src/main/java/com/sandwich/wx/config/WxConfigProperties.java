package com.sandwich.wx.config;

import java.util.Map;

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

    /**
     * 关注回复语
     */
    private String subscribeReply;

    /**
     * 关键字自动回复配置 key:关键字 value:回复内容
     */
    private Map<String, String> autoReply;

}