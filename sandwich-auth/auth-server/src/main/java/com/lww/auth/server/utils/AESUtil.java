package com.lww.auth.server.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES加密解密工具类
 *
 * @author lww
 * @since 2024-12-16
 */
@Component
public class AESUtil {

    // AES算法
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    // AES密钥 - 与前端保持一致
    private static final String SECRET_KEY = "8F6B2CK33DZE20A08O74C231B47AC8F9";

    /**
     * 使用AES解密
     *
     * @param encryptedText 加密的文本
     * @return 解密后的文本
     */
    public static String decrypt(String encryptedText) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 解密密码
     *
     * @param encryptedPassword 加密的密码
     * @return 解密后的密码
     */
    public static String decryptPassword(String encryptedPassword) {
        return decrypt(encryptedPassword);
    }
}