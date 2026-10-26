package com.lww.oss.controller;


import com.lww.web.support.response.ResponseResult;
import com.lww.web.support.response.ResultUtil;
import com.lww.oss.service.FileOssService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作controller
 *
 * @author: lww
 * @since: 2023/10/14
 */
@RestController
@RequestMapping("/api/oss")
public class FileOssController {

    @Resource
    private FileOssService fileService;

    /**
     * 上传文件，返回桶内对象 key（不含桶名），业务侧需自行入库保存
     *
     * @param dir 业务目录，例如 avatar、contract；为空时按日期归档
     */
    @PostMapping("/upload")
    public ResponseResult<String> upload(@RequestPart("uploadFile") MultipartFile uploadFile, String bucket, String dir) {
        String objectKey = fileService.uploadFile(uploadFile, bucket, dir);
        return ResultUtil.success(objectKey);
    }

}
