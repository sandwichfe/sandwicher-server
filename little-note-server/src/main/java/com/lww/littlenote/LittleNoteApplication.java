package com.lww.littlenote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author lww
 */
@MapperScan("com.lww.littlenote.mapper")
@SpringBootApplication
public class LittleNoteApplication {

    public static void main(String[] args) {
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "1089");
        SpringApplication.run(LittleNoteApplication.class, args);
    }

}
