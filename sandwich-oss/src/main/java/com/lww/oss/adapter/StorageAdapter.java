package com.lww.oss.adapter;

import com.lww.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件存储适配器
 *
 * @author lww
 * @since 2023/10/14
 */
public interface StorageAdapter {

    /**
     * 创建bucket桶
     * @param bucket 要创建的桶名称，需符合存储系统的命名规范
     */
    void createBucket(String bucket);

    /**
     * 上传文件到指定存储桶
     * @param uploadFile 要上传的MultipartFile文件对象
     * @param bucket 目标存储桶名称
     * @param objectName 文件在存储桶中的唯一标识/路径
     */
    void uploadFile(MultipartFile uploadFile, String bucket, String objectName);

    /**
     * 列出所有桶
     */
    List<String> getAllBucket();

    /**
     * 获取指定桶中的所有文件信息
     * @param bucket 要查询的桶名称
     * @return 包含文件信息的列表，每个文件信息包括名称、大小、修改时间等
     */
    List<FileInfo> getAllFile(String bucket);

    /**
     * 下载指定桶中的文件
     * @param bucket 文件所在的桶名称
     * @param objectName 要下载的文件标识/路径
     * @return 文件内容输入流
     */
    InputStream downLoad(String bucket, String objectName);

    /**
     * 删除指定的存储桶
     * @param bucket 要删除的桶名称，桶必须为空才能删除
     */
    void deleteBucket(String bucket);

    /**
     * 删除指定桶中的文件对象
     * @param bucket 文件所在的桶名称
     * @param objectName 要删除的文件标识/路径
     */
    void deleteObject(String bucket, String objectName);

    /**
     * 获取指定文件的访问URL
     * @param bucket 文件所在的桶名称
     * @param objectName 文件标识/路径
     * @return 文件的访问URL，可能有时效性
     */
    String getUrl(String bucket, String objectName);
}