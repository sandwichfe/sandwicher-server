package com.lww.littlenote.config.api;

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
public class ApiLittleNotePrefixConfig implements WebMvcConfigurer {

    public static final String API_PREFIX = "/api/little-note";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(API_PREFIX, c -> AnnotationUtils.findAnnotation(c, ApiLittleNotePrefix.class) != null);
    }

}
