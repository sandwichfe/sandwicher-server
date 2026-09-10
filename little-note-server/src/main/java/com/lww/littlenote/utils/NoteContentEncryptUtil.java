package com.lww.littlenote.utils;

import com.lww.web.support.exception.AppException;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 笔记正文加解密工具。
 *
 * @author lww
 * @since 2026-07-03
 */
public final class NoteContentEncryptUtil {

    private NoteContentEncryptUtil() {
    }

    /**
     * AES 算法，与前端加密规则保持一致。
     */
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 前端加密笔记正文使用的密钥。
     */
    private static final String SECRET_KEY = "8F6B2CK33DZE20A08O74C231B47AC8F9";

    /**
     * 解密前端传入的笔记正文密文。
     *
     * @param encryptedContent Base64 编码后的 AES 密文
     * @return 解密后的正文；空内容原样返回
     */
    public static String decryptContent(String encryptedContent) {
        if (!StringUtils.hasText(encryptedContent)) {
            return encryptedContent;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedContent));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AppException("笔记正文解密失败");
        }
    }
}
