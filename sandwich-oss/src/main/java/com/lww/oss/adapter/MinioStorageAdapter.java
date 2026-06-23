package com.lww.oss.adapter;

import com.lww.common.web.exception.AppException;
import com.lww.oss.entity.FileInfo;
import com.lww.oss.util.MinioUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * MinIO storage adapter.
 *
 * @author lww
 * @since 2023/10/14
 */
public class MinioStorageAdapter implements StorageAdapter {

    @Resource
    private MinioUtil minioUtil;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public void createBucket(String bucket) {
        minioUtil.createBucket(bucket);
    }

    @Override
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        if (!StringUtils.hasText(bucket)) {
            bucket = this.bucket;
        }
        minioUtil.createBucket(bucket);

        String fileName = resolveFileName(uploadFile);
        String objectName = StringUtils.hasText(dir) ? dir + "/" + fileName : fileName;

        try (InputStream inputStream = uploadFile.getInputStream()) {
            minioUtil.uploadFile(
                    inputStream,
                    bucket,
                    objectName,
                    uploadFile.getSize(),
                    uploadFile.getContentType()
            );
        } catch (IOException exception) {
            throw new AppException("Upload file stream failed, fileName=" + fileName + ", reason=" + exception.getMessage());
        }
        return bucket + "/" + objectName;
    }

    @Override
    public List<String> getAllBucket() {
        return minioUtil.getAllBucket();
    }

    @Override
    public List<FileInfo> getAllFile(String bucket) {
        return minioUtil.getAllFile(bucket);
    }

    @Override
    public InputStream download(String bucket, String objectName) {
        return minioUtil.download(bucket, objectName);
    }

    @Override
    public void deleteBucket(String bucket) {
        minioUtil.deleteBucket(bucket);
    }

    @Override
    public void deleteObject(String bucket, String objectName) {
        minioUtil.deleteObject(bucket, objectName);
    }

    @Override
    public String getUrl(String bucket, String objectName) {
        return url + "/" + bucket + "/" + objectName;
    }

    private String resolveFileName(MultipartFile uploadFile) {
        String originalFilename = uploadFile.getOriginalFilename();
        if (StringUtils.hasText(originalFilename)) {
            return originalFilename;
        }
        return uploadFile.getName();
    }
}
