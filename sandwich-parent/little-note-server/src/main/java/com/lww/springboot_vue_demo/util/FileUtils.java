package com.lww.springboot_vue_demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @description: 文件操作相关工具类
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
        log.info(file.getPath() + "  delete success!");
    }

    

}
