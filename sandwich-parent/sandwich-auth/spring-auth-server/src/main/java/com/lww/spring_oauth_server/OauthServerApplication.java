package com.lww.spring_oauth_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * todo
 *
 * @author lww
 * @since 2024/11/13
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }
}
