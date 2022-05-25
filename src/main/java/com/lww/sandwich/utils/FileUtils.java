package com.lww.sandwich.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @description: 文件处理工具类
 * @author lww
 * @since 2022/5/25 10:29
 */
public class FileUtils {


    /**
     * InputStream转成文件
     * @author lww
     * @since 2022/5/25 10:35
     * @param is
     * @return File
     */
    public static void inputStreamToFile(InputStream is,File file) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        byte[] b = new byte[1024];
        while ((is.read(b)) != -1) {
            // 写入数据
            fos.write(b);
        }
        is.close();
        fos.close();
        // 保存数据
    }
}
