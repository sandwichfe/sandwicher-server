package com.lww.sandwich.utils;

/**
 *  
 * @author lww
 * @since 2023/7/17 13:16
 */

import org.junit.Test;

/**
 * AES加解密测试类
 * @author zhuquanwen
 **/
public class AesUtilsTests {

    /**
     * 测试AES 默认加密
     * */
    @Test
    public void aesEncrpty() throws Exception {
        String sec = AesUtil.aesEncrypt("USER2");
        System.out.println(sec);
    }

    /**
     *
     * 测试AES默认解密
     * */
    @Test
    public void aesDecrpty() throws Exception {
        String ori = AesUtil.aesDecrypt("Cg2jBQvUGJJUMfalO+HF5g==");
    }

    /**
     * 测试AES 带KEY加密
     * */
    @Test
    public void aesEncrptyWithKey() throws Exception {
        String k = "6x9o67h5BO205Cfv";
        String sec = AesUtil.aesEncrypt("admin", k);
    }

    /**
     * 测试AES 带KEY解密
     * */
    @Test
    public void aesDecrptyWithKey() throws Exception {
        String k = "6x9o67h5BO205Cfv";
        String ori = AesUtil.aesDecrypt("yij1zaxI6X10t7v6OpW7gw==", k);
    }

}


