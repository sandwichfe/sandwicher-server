package com.lww.oss.service;


import java.util.List;

import com.lww.oss.adapter.StorageAdapter;
import org.springframework.stereotype.Service;
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
     * 上传文件，返回桶内对象 key
     * 日期目录由存储适配器统一生成，dir 只表示业务目录，不传时直接按日期归档
     */
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        return storageAdapter.uploadFile(uploadFile, bucket, dir);
    }

}

