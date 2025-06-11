package com.lww.oss.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lww.oss.adapter.StorageAdapter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储service
 *
 * @author: lww
 * @since: 2023/10/14
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
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        // dir 不传默认 为 当前年/月/日
        String objectKey = StringUtils.hasText(dir) ? dir : DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now());
        storageAdapter.uploadFile(uploadFile, bucket, objectKey);
        // 返回标准化路径（bucket/key格式）
        return bucket+"/"+objectKey + "/" + uploadFile.getOriginalFilename();
    }

}


