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

    @Override
    public String getDefaultBucket() {
        return defaultBucket;
    }

    /**
     * 上传文件，返回桶内对象 key，例如 2026/10/26/xxx.png；不含桶名、域名和前导斜杠。
     * 桶名由调用方从请求参数或配置中获取，访问地址由调用方按「前缀 + key」拼装，不要把地址入库。
     */
    @Override
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        // 先确定目标桶并确保其存在
        String targetBucket = StringUtils.hasText(bucket) ? bucket : defaultBucket;
        rustfsUtil.createBucket(targetBucket);

        // 生成对象 key，唯一标识保证不重名，无需上传前查重；dir 先归一化，避免桶名混进 key
        String objectName = buildObjectName(normalizeDir(dir, targetBucket), resolveFileName(uploadFile));

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
        // 返回裸 key：前导 / 是 URL 语义，不属于对象 key
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
        return rustfsUtil.download(bucket, stripLeadingSlash(objectName));
    }

    @Override
    public void deleteBucket(String bucket) {
        rustfsUtil.deleteBucket(bucket);
    }

    @Override
    public void deleteObject(String bucket, String objectName) {
        rustfsUtil.deleteObject(bucket, stripLeadingSlash(objectName));
    }

    /**
     * 获取预签名地址，未指定桶时与上传保持一致，回落到默认桶。
     */
    @Override
    public String getPresignedUrl(String bucket, String objectName, Duration duration) {
        String targetBucket = StringUtils.hasText(bucket) ? bucket : defaultBucket;
        return rustfsUtil.getPresignedUrl(targetBucket, stripLeadingSlash(objectName), duration);
    }

    @Override
    public FileInfo statObject(String bucket, String objectName) {
        return rustfsUtil.statObject(bucket, stripLeadingSlash(objectName));
    }

    @Override
    public boolean objectExists(String bucket, String objectName) {
        return rustfsUtil.objectExists(bucket, stripLeadingSlash(objectName));
    }

    /**
     * 对外统一使用带前导 / 的 key，调用存储 SDK 前去掉，避免出现 //2026/... 这样的双斜杠路径。
     */
    private String stripLeadingSlash(String objectName) {
        if (!StringUtils.hasText(objectName)) {
            return objectName;
        }
        String normalized = objectName.trim();
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        return normalized;
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
     * 业务目录归一化：去掉首尾斜杠；若调用方误把桶名当成业务目录传入（例如 dir=yiru），直接剥掉，
     * 保证返回的对象 key 只包含桶内路径，调用方拼访问地址时不会出现 /桶名/桶名/...。
     *
     * @param dir    业务目录，例如 avatar、contract
     * @param bucket 目标桶名
     * @author lww
     * @since 2026-10-26
     */
    private String normalizeDir(String dir, String bucket) {
        if (!StringUtils.hasText(dir)) {
            return dir;
        }
        String normalized = dir.trim();
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (normalized.contains("..")) {
            throw new AppException("业务目录不合法：" + dir);
        }
        if (!StringUtils.hasText(bucket)) {
            return normalized;
        }
        if (normalized.equals(bucket)) {
            return "";
        }
        String bucketPrefix = bucket + "/";
        return normalized.startsWith(bucketPrefix) ? normalized.substring(bucketPrefix.length()) : normalized;
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
