package com.lww.oss.adapter;

import com.lww.web.support.exception.AppException;
import com.lww.oss.entity.FileInfo;
import com.lww.oss.util.MinioUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * MinIO storage adapter.
 *
 * @author lww
 * @since 2023/10/14
 */
public class MinioStorageAdapter implements StorageAdapter {

    /**
     * 重名文件拼接的时间戳格式
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

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
        // 生成上传路径
        String objectName = resolveUniqueObjectName(bucket, dir, fileName);

        try (InputStream inputStream = uploadFile.getInputStream()) {
            minioUtil.uploadFile(
                    inputStream,
                    bucket,
                    objectName,
                    uploadFile.getSize(),
                    uploadFile.getContentType()
            );
        } catch (IOException exception) {
            throw new AppException("Upload file stream failed, objectName=" + objectName + ", reason=" + exception.getMessage());
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

    /**
     * 生成 objectName：若同名对象已存在，则在文件名后拼接时间戳（保留扩展名）
     *
     * @author lww
     * @since 2026-10-20
     */
    private String resolveUniqueObjectName(String bucket, String dir, String fileName) {
        String objectName = buildObjectName(dir, fileName);
        // 若对象不存在，则直接返回 objectName
        if (!minioUtil.objectExists(bucket, objectName)) {
            return objectName;
        }

        // 重命名规则：原文件名 + "_" + 时间戳，保留扩展名
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
        String extension = dotIndex > 0 ? fileName.substring(dotIndex) : "";
        return buildObjectName(dir, baseName + "_" + timestamp + extension);
    }

    private String buildObjectName(String dir, String fileName) {
        return StringUtils.hasText(dir) ? dir + "/" + fileName : fileName;
    }
}
