package com.lww.sandwicherserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author lww
 * @author lww
 * @Date 2020-3-7
 */
@SpringBootApplication
@EnableOpenApi
public class SandwichServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandwichServerApplication.class, args);
    }

}
