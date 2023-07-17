package com.lww.sandwich.config.jasyptConfig;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

import static com.lww.sandwich.config.jasyptConfig.JasptyConstants.PBE_ALGORITHMS_MD5_DES;

/**
 * @description: jaspy加密配置   自定义加密器
 * @author lww
 * @since 2022/11/21 10:04
 */
public class MyStringEncryptor implements StringEncryptor {



    /**
     * 加解密盐值
     */
    private String password;
    /**
     * 加解密算法
     */
    private String algorithm = PBE_ALGORITHMS_MD5_DES;

    public MyStringEncryptor(String password) {
        this.password = password;
    }

    public MyStringEncryptor(String password, String algorithm) {
        this.password = password;
        this.algorithm = algorithm;
    }

    @Override
    public String encrypt(String message) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        // 加解密盐值
        encryptor.setConfig(getConfig(this.password));
        return encryptor.encrypt(message);
    }

    @Override
    public String decrypt(String encryptedMessage) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(getConfig(this.password));
        return encryptor.decrypt(encryptedMessage);
    }

    public SimpleStringPBEConfig getConfig(String password) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 加密盐值
        config.setPassword(password);
        // 加解密算法
        config.setAlgorithm(PBE_ALGORITHMS_MD5_DES);
        // 设置密钥获取迭代次数
        config.setKeyObtentionIterations(1000);
        // 线程池大小：默认1
        config.setPoolSize(1);
        // 盐值生成器className
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        //  iv(initialization vector，初始化向量) 生成器className
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        // 设置字符串输出类型
        config.setStringOutputType("base64");
        return config;
    }
}
