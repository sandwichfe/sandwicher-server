package com.sandwich.wx;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 微信服务启动器
 *
 * @author lww
 * @since 2023/10/11
 */
@SpringBootApplication
@ComponentScan("com.sandwich")
public class WxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxApplication.class, args);
    }

}

