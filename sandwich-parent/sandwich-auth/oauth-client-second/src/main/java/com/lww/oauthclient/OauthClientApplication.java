package com.lww.oauthclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Hello world!
 *
 * @author lww
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OauthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthClientApplication.class, args);
    }
}

