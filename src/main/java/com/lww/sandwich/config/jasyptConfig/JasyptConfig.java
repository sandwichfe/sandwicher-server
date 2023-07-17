package com.lww.sandwich.config.jasyptConfig;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author lww
 * @since 2022/11/21 10:09
 */
@Configuration
public class JasyptConfig {
    /**
     * 加解密盐值
     */
    //@Value("${jasypt.encryptor.password}")
    private String password;

    @Bean("myStringEncryptor")
    public StringEncryptor myStringEncryptor() {
        return new MyStringEncryptor("lww");
    }
}
