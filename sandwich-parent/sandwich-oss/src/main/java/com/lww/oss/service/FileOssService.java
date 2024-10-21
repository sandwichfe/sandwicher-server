package com.lww.oss.service;


import com.lww.oss.adapter.StorageAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件存储service
 *
 * @author: lww
 * @date: 2023/10/14
 */
@Service
public class FileOssService {

    private final StorageAdapter storageAdapter;

    /**
     *  构造器方式注入storageAdapter   final修饰的 采用构造器方式
     */
    public FileOssService(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }

    /**
     * 列出所有桶
     */
    public List<String> getAllBucket() {
        return storageAdapter.getAllBucket();
    }

    /**
     * 获取文件路径
     */
    public String getUrl(String bucketName,String objectName) {
        return storageAdapter.getUrl(bucketName,objectName);
    }

    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir){
        storageAdapter.uploadFile(uploadFile,bucket,dir);
        return storageAdapter.getUrl(bucket,  dir + "/" + uploadFile.getOriginalFilename());
    }
}


