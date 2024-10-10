package com.lww.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 忽略权限校验接口
 * @author lww
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "security.permit")
public class SecurityPermit {

    private List<String> urls = new ArrayList<>();

}
