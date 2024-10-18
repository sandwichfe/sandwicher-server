package com.lww.oss.controller;


import com.lww.oss.entity.Result;
import com.lww.oss.service.FileOssService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件操作controller
 *
 * @author: ChickenWing
 * @date: 2023/10/14
 */
@Slf4j
@RestController
public class FileOssController {

    @Resource
    private FileOssService fileService;

    @Resource
    private StringEncryptor stringEncryptor;

    @GetMapping("/list-oss")
    public String testGetAllBuckets() {
        List<String> allBucket = fileService.getAllBucket();
        return allBucket.get(0);
    }

    @GetMapping("/getUrl")
    public String getUrl(String bucketName, String objectName) throws Exception {
        return fileService.getUrl(bucketName, objectName);
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile uploadFile, String bucket, String objectName) throws Exception {
        String url = fileService.uploadFile(uploadFile, bucket, objectName);
        return Result.ok(url);
    }

}

