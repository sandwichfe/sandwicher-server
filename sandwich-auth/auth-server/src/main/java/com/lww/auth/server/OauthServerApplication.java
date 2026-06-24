package com.lww.auth.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * @author lww
 */
@MapperScan({"com.lww.auth.server.portal.mapper",
        "com.lww.common.web.log.mapper"
})
@SpringBootApplication(scanBasePackages = "com.lww")
public class OauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }

}

