package com.lww.sandwicherserver;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author lww
 * @Date 2020-3-7
 */
@SpringBootApplication
@EnableOpenApi
@EnableKnife4j
public class SandwichServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandwichServerApplication.class, args);
    }

}
