package com.lww.littlenote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author lww
 */
@MapperScan("com.lww.littlenote.mapper")
@EnableScheduling
@SpringBootApplication
public class LittleNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LittleNoteApplication.class, args);
    }

}
