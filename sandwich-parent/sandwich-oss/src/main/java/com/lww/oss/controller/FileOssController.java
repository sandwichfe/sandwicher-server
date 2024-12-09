package com.lww.oss.controller;


import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.oss.service.FileOssService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件操作controller
 *
 * @author: lww
 * @since: 2023/10/14
 */
@Slf4j
@RestController
public class FileOssController {

    @Resource
    private FileOssService fileService;

    @Resource
    private StringEncryptor stringEncryptor;

    @GetMapping("/list-oss")
    public ResponseResult<List<String>> testGetAllBuckets() {
        List<String> allBucket = fileService.getAllBucket();
        return ResultUtil.success(allBucket);
    }

    @GetMapping("/getUrl")
    public ResponseResult<String> getUrl(String bucketName, String objectName) {
        return ResultUtil.success(fileService.getUrl(bucketName, objectName));
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseResult<String> upload(@RequestPart("uploadFile") MultipartFile uploadFile, String bucket, String objectName) {
        String url = fileService.uploadFile(uploadFile, bucket, objectName);
        return ResultUtil.success(url);
    }

}

