package com.lww.auth.server.portal.config.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * todo
 *
 * @author lww
 * @since 2025/8/25
 */

@Configuration
public class ApiPortalPrefixConfig implements WebMvcConfigurer {

    public static final String API_PREFIX = "/api/portal";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(API_PREFIX, c -> AnnotationUtils.findAnnotation(c, ApiPortalPrefix.class) != null);
    }

}
