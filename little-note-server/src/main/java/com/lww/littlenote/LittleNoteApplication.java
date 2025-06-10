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
        SpringApplication.run(LittleNoteApplication.class, args);
    }

}
