package com.lww.littlenote.service;

import java.util.List;
import java.util.Map;

/**
 *   fileService
 * @author lww
 * @since 2022/9/5 14:56
 */
public interface FileService {

    /** 
     * 文件目录列表
     * @author lww
     * @since 2022/9/5 15:06
     * @param path path
	 * @param keyword keyword
     * @return List
     */
    List<Map<String, String>> getFileList(String path, String keyword);

    /**
     * 重命名 文件/文件夹
     * @author lww
     * @since 2022/9/5 16:03
     * @param path path
     * @param newName path
     */
    void renameFile(String path,String newName);

    /**
     * 创建文件夹
     * @author lww
     * @since 2022/9/28 13:43
     * @param path path
	 * @param dirName dirName
     */
    void mkdir(String path, String dirName);
}
