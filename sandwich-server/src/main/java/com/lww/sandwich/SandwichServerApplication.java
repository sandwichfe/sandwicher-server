package com.lww.sandwich;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lww
 * @since 2022-3-7
 */
@SpringBootApplication
@MapperScan("com.lww.sandwich.mapper")
public class SandwichServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SandwichServerApplication.class, args);
    }
}
