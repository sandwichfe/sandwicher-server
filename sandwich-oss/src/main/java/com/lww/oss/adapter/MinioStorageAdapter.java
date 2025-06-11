package com.lww.oss.adapter;

import com.lww.oss.entity.FileInfo;
import com.lww.oss.util.MinioUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * minioIO存储适配器
 *
 * @author lww
 * @since 2023/10/14
 */
public class MinioStorageAdapter implements StorageAdapter {

    @Resource
    private MinioUtil minioUtil;

    /**
     * minioUrl
     */
    @Value("${minio.url}")
    private String url;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    @SneakyThrows
    public void createBucket(String bucket) {
        minioUtil.createBucket(bucket);
    }

    @Override
    @SneakyThrows
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        if (!StringUtils.hasText(bucket)){
            bucket = this.bucket;
        }
        minioUtil.createBucket(bucket);
        if (dir != null) {
            minioUtil.uploadFile(uploadFile.getInputStream(), bucket, dir + "/" + uploadFile.getOriginalFilename());
        } else {
            minioUtil.uploadFile(uploadFile.getInputStream(), bucket, uploadFile.getOriginalFilename());
        }
        // 返回标准化路径（bucket/key格式）
        return bucket+"/"+dir + "/" + uploadFile.getOriginalFilename();
    }

    @Override
    @SneakyThrows
    public List<String> getAllBucket() {
        return minioUtil.getAllBucket();
    }

    @Override
    @SneakyThrows
    public List<FileInfo> getAllFile(String bucket) {
        return minioUtil.getAllFile(bucket);
    }

    @Override
    @SneakyThrows
    public InputStream downLoad(String bucket, String objectName) {
        return minioUtil.downLoad(bucket, objectName);
    }

    @Override
    @SneakyThrows
    public void deleteBucket(String bucket) {
        minioUtil.deleteBucket(bucket);
    }

    @Override
    @SneakyThrows
    public void deleteObject(String bucket, String objectName) {
        minioUtil.deleteObject(bucket, objectName);
    }

    @Override
    @SneakyThrows
    public String getUrl(String bucket, String objectName) {
        return url + "/" + bucket + "/" + objectName;
    }


}
