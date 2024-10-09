package com.lww.littlenote.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 *   文件操作相关工具类
 * @author lww
 * @since 2022/9/5 15:57
 */
@Slf4j
public class FileUtils {

    /**
     * 递归删除
     * 删除某个目录及目录下的所有子目录和文件
     * @param file 文件或目录
     * @return 删除结果
     */
    public static void delFiles(File file) {
        //目录
        if (file.isDirectory()) {
            File[] childrenFiles = file.listFiles();
            for (File childFile : childrenFiles) {
                delFiles(childFile);
            }
        }
        //删除 文件、空目录
        file.delete();
        log.info("{}  delete success!", file.getPath());
    }

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
