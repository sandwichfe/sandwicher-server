package com.lww.littlenote.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * @author sandw
 */
@Slf4j
public class AesUtil {

    /**
     * AES加密
     *
     * @param content 明文
     * @param key     秘钥
     */
    public static String encrypt(Object content, String key) {
        String s ="";
        //判断content是否为字符串
        if(content instanceof String){
            s=content.toString();
        }else {
            s = JSONUtil.parse(content).toString();
        }
        // 将返回的加密过的 byte[] 转换成Base64编码字符串 ！！！！很关键
        return base64ToString(aesEcbEncrypt(s.getBytes(), key.getBytes()));
    }

    /**
     * AES解密
     *
     * @param content Base64编码的密文
     * @param key     秘钥
     */
    public static Object decrypt(String content, String key) {
        // stringToBase64() 将 Base64编码的字符串转换成 byte[] !!!与base64ToString(）配套使用
        try {
            byte[] base64 = stringToBase64(content);
            byte[] bytes = aesEcbDecrypt(base64, key.getBytes());
            String result = new String(bytes);
            String s = result.replaceAll("\"", "");
            //判断解密出来的数据是字符串还是json
            if(s.startsWith("{") && s.endsWith("}")){
                return JSONUtil.parse(s);
            }else{
                return s;
            }
        } catch (Exception e) {
            log.info("AES解密出错！！！",e);
        }

        return null;
    }

    private static byte[] aesEcbEncrypt(byte[] content, byte[] keyBytes) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
           log.info("error",e);
        }
        return new byte[0];
    }

    private static byte[] aesEcbDecrypt(byte[] content, byte[] keyBytes) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.info("error",e);
        }
        return new byte[0];
    }

    /**
     * 字符串装换成 Base64
     */

    public static byte[] stringToBase64(String key) {
        return Base64.decodeBase64(key.getBytes());
    }

    /**
     * Base64装换成字符串
     */
    public static String base64ToString(byte[] key) {
        return new Base64().encodeToString(key);
    }

    public static void main(String[] args) throws Exception {
        // 加密密钥,很关键，不要对外泄露哦
        String key = "8F6B2CK33DZE20A08O74C231B47AC8F9";
        // 明文
        String content = "hello";

        String end = encrypt(content, key);
        log.info("加密：{}", end);

        String decrypt = decrypt(end, key).toString();
        log.info("解密：{}", decrypt);
    }
}


