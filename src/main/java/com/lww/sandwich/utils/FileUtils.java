package com.lww.sandwich.utils;

import java.io.*;

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
    public static void inputStreamToFile(InputStream is, File file) throws Exception {
        FileReader fr = new FileReader(file);
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        StringBuffer sb = new StringBuffer();
        while (isr.ready()) {
            sb.append((char) isr.read());
        }
        isr.close();
        fr.close();
    }

    /**
     * @功能 读取流
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

}
