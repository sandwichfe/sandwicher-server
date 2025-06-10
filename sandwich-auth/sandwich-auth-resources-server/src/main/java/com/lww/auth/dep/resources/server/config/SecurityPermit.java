package com.lww.auth.dep.resources.server.config;

/**
 * 忽略权限校验接口
 * @author lww
 * @since 2024/12/9
 */
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.permit")
public class SecurityPermit {

    private List<String> urls = new ArrayList<>();

}
