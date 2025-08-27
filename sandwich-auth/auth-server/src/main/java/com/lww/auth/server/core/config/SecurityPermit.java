package com.lww.auth.server.core.config;

/**
 * 忽略权限校验接口
 * @author lww
 * @since 2024/12/9
 */

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.permit")
public class SecurityPermit {

    private List<String> urls = new ArrayList<>();

}
