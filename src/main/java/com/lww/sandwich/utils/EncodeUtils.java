package com.lww.sandwich.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @description: 编码转换工具类
 * @author lww
 * @since 2022/5/25 11:03
 */
@Slf4j
public class EncodeUtils {

    /**
     * 将GBK编码转换成UTF-8编码
     *
     */
    protected String gbkConvertToUtf8(String str) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(str)) {
            //转换成gbk编码
            String gbkChinese = new String(str.getBytes("GBK"), StandardCharsets.ISO_8859_1);

            String unicodeChinese = new String(gbkChinese.getBytes(StandardCharsets.ISO_8859_1), "GBK");
            //中文
            log.info(unicodeChinese);
            //utf--8编码
            String utf8Chinese = new String(unicodeChinese.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            //乱码
            log.info(utf8Chinese);
            unicodeChinese = new String(utf8Chinese.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //中文
            log.info(unicodeChinese);
            return unicodeChinese;
        }else{
            return null;
        }

    }

    @Test
    public void test() throws UnsupportedEncodingException {
        System.out.println(gbkConvertToUtf8("����a�\u0003\u001D"));
    }
}
