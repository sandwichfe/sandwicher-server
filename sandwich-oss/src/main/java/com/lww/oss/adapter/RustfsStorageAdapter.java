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
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * RustFS storage adapter，保持现有文件存储接口和上传路径规则。
 *
 * @author lww
 * @since 2023/10/14
 */
public class RustfsStorageAdapter implements StorageAdapter {

    /**
     * 对象 key 中的日期目录格式，按上传日期归档
     */
    private static final DateTimeFormatter DATE_PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 扩展名允许的最大长度，超长视为非法扩展名
     */
    private static final int MAX_EXTENSION_LENGTH = 10;

    @Resource
    private RustfsUtil rustfsUtil;

    /**
     * 上传未指定桶时使用的默认桶
     */
    @Value("${rustfs.bucket}")
    private String defaultBucket;

    @Override
    public void createBucket(String bucket) {
        rustfsUtil.createBucket(bucket);
    }

    /**
     * 上传文件，返回桶内对象 key；桶信息不回传，由调用方从请求参数或配置中获取。
     */
    @Override
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        // 先确定目标桶并确保其存在
        String targetBucket = StringUtils.hasText(bucket) ? bucket : defaultBucket;
        rustfsUtil.createBucket(targetBucket);

        // 生成对象 key，唯一标识保证不重名，无需上传前查重
        String objectName = buildObjectName(dir, resolveFileName(uploadFile));

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
        return objectName;
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
    public String getPresignedUrl(String bucket, String objectName, Duration duration) {
        return rustfsUtil.getPresignedUrl(bucket, objectName, duration);
    }

    @Override
    public FileInfo statObject(String bucket, String objectName) {
        return rustfsUtil.statObject(bucket, objectName);
    }

    @Override
    public boolean objectExists(String bucket, String objectName) {
        return rustfsUtil.objectExists(bucket, objectName);
    }

    /**
     * 优先使用原始文件名，缺失时沿用上传字段名。
     * 原始文件名仅用于提取扩展名，完整名称由业务侧入库保存。
     */
    private String resolveFileName(MultipartFile uploadFile) {
        String originalFilename = uploadFile.getOriginalFilename();
        if (StringUtils.hasText(originalFilename)) {
            return originalFilename;
        }
        return uploadFile.getName();
    }

    /**
     * 生成对象 key：{业务目录}/{yyyy}/{MM}/{dd}/{唯一标识}.{扩展名}。
     * 使用唯一标识替代原始文件名，规避中文、空格、路径分隔符等字符的编码与路径穿越问题，
     * 同时天然避免并发上传同名覆盖，无需上传前 headObject 查重。
     *
     * @param dir 业务目录，例如 avatar、contract；为空时按日期直接归档
     * @author lww
     * @since 2026-10-26
     */
    private String buildObjectName(String dir, String fileName) {
        String datePath = LocalDate.now().format(DATE_PATH_FORMATTER);
        String uniqueName = UUID.randomUUID().toString().replace("-", "") + resolveExtension(fileName);
        return StringUtils.hasText(dir) ? dir + "/" + datePath + "/" + uniqueName : datePath + "/" + uniqueName;
    }

    /**
     * 提取并校验扩展名，仅保留字母数字且长度合法的扩展名，非法时返回空串。
     *
     * @author lww
     * @since 2026-10-26
     */
    private String resolveExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex <= 0 || dotIndex >= fileName.length() - 1) {
            return "";
        }
        String extension = fileName.substring(dotIndex + 1);
        if (extension.length() > MAX_EXTENSION_LENGTH || !extension.chars().allMatch(Character::isLetterOrDigit)) {
            return "";
        }
        return "." + extension.toLowerCase(Locale.ROOT);
    }
}
