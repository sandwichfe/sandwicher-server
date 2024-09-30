package com.lww.sandwich.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @description 编码转换工具类
 * @author lww
 * @since 2022/5/25 11:03
 */
@Slf4j
public class EncodeUtils {


    @Test
    public void test() throws UnsupportedEncodingException {
        String str= "����a������XM����ʴ�����5D ���?�ʞ\\@���o��5Dp�����ʴ@�����5D����ʳ���\n" +
                "                                                                                                            �";
        String [] charset=new String[] {"gbk","unicode","utf8","gb2312","ISO-8859-1"};
        for (int i=0;i<charset.length;i++){
            for (int j=0;j<charset.length;j++){
                System.out.println("二进制格式:   "+charset[i]+"编码格式:  "+charset[j]);
                System.out.println("编码后的字符串:  "+new String(str.getBytes(charset[i]),charset[j]));

            }
        }
    }
}
