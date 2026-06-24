package com.lww.auth.server.portal.config.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RestController;

/**
 * 固定前缀 /api/portal
 *
 * @author lww
 * @since 2026/6/24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiPortalPrefix
@RestController
public @interface ApiPortalRestController {

}
