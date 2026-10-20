package com.lww.oss.adapter;

import com.lww.web.support.exception.AppException;
import com.lww.oss.entity.FileInfo;
import com.lww.oss.util.RustfsUtil;
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
 * RustFS storage adapter，保持现有文件存储接口和上传路径规则。
 *
 * @author lww
 * @since 2023/10/14
 */
public class RustfsStorageAdapter implements StorageAdapter {

    /**
     * 重名文件拼接的时间戳格式
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 原始名称重名后，最多检查 10 个候选名称，避免无限查重。
     */
    private static final int MAX_RENAME_ATTEMPTS = 10;

    @Resource
    private RustfsUtil rustfsUtil;

    @Value("${rustfs.url}")
    private String url;

    /**
     * 上传未指定桶时使用的默认桶
     */
    @Value("${rustfs.bucket}")
    private String defaultBucket;

    @Override
    public void createBucket(String bucket) {
        rustfsUtil.createBucket(bucket);
    }

    @Override
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        // 先确定目标桶并确保其存在，再判断对象是否重名。
        String targetBucket = StringUtils.hasText(bucket) ? bucket : defaultBucket;
        rustfsUtil.createBucket(targetBucket);

        String fileName = resolveFileName(uploadFile);
        // 生成上传路径，重名处理统一由路径解析方法负责。
        String objectName = resolveUniqueObjectName(targetBucket, dir, fileName);

        try (InputStream inputStream = uploadFile.getInputStream()) {
            rustfsUtil.uploadFile(
                    inputStream,
                    targetBucket,
                    objectName,
                    uploadFile.getSize(),
                    uploadFile.getContentType()
            );
        } catch (IOException exception) {
            throw new AppException("Upload file stream failed, objectName=" + objectName + ", reason=" + exception.getMessage());
        }
        return targetBucket + "/" + objectName;
    }

    @Override
    public List<String> getAllBucket() {
        return rustfsUtil.getAllBucket();
    }

    @Override
    public List<FileInfo> getAllFile(String bucket) {
        return rustfsUtil.getAllFile(bucket);
    }

    @Override
    public InputStream download(String bucket, String objectName) {
        return rustfsUtil.download(bucket, objectName);
    }

    @Override
    public void deleteBucket(String bucket) {
        rustfsUtil.deleteBucket(bucket);
    }

    @Override
    public void deleteObject(String bucket, String objectName) {
        rustfsUtil.deleteObject(bucket, objectName);
    }

    @Override
    public String getUrl(String bucket, String objectName) {
        return url + "/" + bucket + "/" + objectName;
    }

    /**
     * 优先使用原始文件名，缺失时沿用上传字段名。
     */
    private String resolveFileName(MultipartFile uploadFile) {
        String originalFilename = uploadFile.getOriginalFilename();
        if (StringUtils.hasText(originalFilename)) {
            return originalFilename;
        }
        return uploadFile.getName();
    }

    /**
     * 生成 objectName：若同名对象已存在，则追加时间戳，候选名称仍冲突时追加序号。
     * 重命名最多尝试 10 次，仍重名时抛出异常，不返回已存在的对象路径。
     * 文件扩展名保持不变；本方法用于上传前查重，不保证并发上传的原子性。
     *
     * @author lww
     * @since 2026-10-20
     */
    private String resolveUniqueObjectName(String bucket, String dir, String fileName) {
        String objectName = buildObjectName(dir, fileName);
        // 若对象不存在，则直接返回 objectName
        if (!rustfsUtil.objectExists(bucket, objectName)) {
            return objectName;
        }

        // 重命名规则：原文件名 + "_" + 时间戳，保留扩展名
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        for (int sequence = 0; sequence < MAX_RENAME_ATTEMPTS; sequence++) {
            // 时间戳名称再次冲突时追加递增序号，并重新检查候选路径。
            String suffix = sequence == 0 ? timestamp : timestamp + "_" + sequence;
            objectName = buildObjectName(dir, appendFileNameSuffix(fileName, suffix));
            if (!rustfsUtil.objectExists(bucket, objectName)) {
                return objectName;
            }
        }
        // 达到上限后终止上传，避免无限循环或使用已存在的候选路径。
        throw new AppException("文件重命名已达到尝试上限（" + MAX_RENAME_ATTEMPTS
                + " 次），请稍后重试，bucket=" + bucket + ", fileName=" + fileName);
    }

    /**
     * 在文件名与扩展名之间插入后缀，无扩展名时直接追加。
     */
    private String appendFileNameSuffix(String fileName, String suffix) {
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
        String extension = dotIndex > 0 ? fileName.substring(dotIndex) : "";
        return baseName + "_" + suffix + extension;
    }

    /**
     * 根据目录和文件名拼接对象路径，不指定目录时直接使用文件名。
     */
    private String buildObjectName(String dir, String fileName) {
        return StringUtils.hasText(dir) ? dir + "/" + fileName : fileName;
    }
}
