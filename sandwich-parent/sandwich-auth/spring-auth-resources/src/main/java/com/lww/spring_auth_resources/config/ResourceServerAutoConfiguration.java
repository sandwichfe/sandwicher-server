package com.lww.spring_auth_resources.config;

/**
 * todo
 *
 * @author lww
 * @since 2024/11/13
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerAutoConfiguration {

    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().anyRequest().authenticated().and()
                .oauth2ResourceServer().jwt();

        return http.build();
    }
}

