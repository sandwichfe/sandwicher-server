package com.lww.oss.service;


import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.lww.oss.adapter.StorageAdapter;
import com.lww.oss.vo.PresignedUrlVo;
import com.lww.web.support.exception.AppException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储service
 *
 * @author: lww
 * @since: 2023/10/14
 */
@Service
public class FileOssService {

    /**
     * 预签名地址默认有效期，业务可按时效要求传入 expireSeconds 覆盖
     */
    private static final Duration DEFAULT_PRESIGN_DURATION = Duration.ofMinutes(30);

    /**
     * S3 预签名有效期上限为 7 天，超过该值服务端会拒绝签名
     */
    private static final Duration MAX_PRESIGN_DURATION = Duration.ofDays(7);

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

    /**
     * 解析实际使用的桶名，未指定时回落到默认桶，保证 key 归一化和签名使用同一个桶
     */
    public String resolveBucket(String bucket) {
        return StringUtils.hasText(bucket) ? bucket : storageAdapter.getDefaultBucket();
    }

    /**
     * 获取单个对象的预签名访问地址，有效期按秒传入，不传时使用默认值
     *
     * @param bucket        桶名，为空时使用默认桶
     * @param objectKey     桶内对象 key，不包含桶名和域名
     * @param expireSeconds 地址有效期（秒），为空时默认 30 分钟，最大 7 天
     */
    public String getPresignedUrl(String bucket, String objectKey, Long expireSeconds) {
        return getPresignedUrl(bucket, objectKey, resolvePresignDuration(expireSeconds));
    }

    /**
     * 获取单个对象的预签名访问地址，使用明确的有效期，302 跳转等固定时长场景直接调用
     *
     * @param duration 地址有效期，由调用方按场景指定
     */
    public String getPresignedUrl(String bucket, String objectKey, Duration duration) {
        return storageAdapter.getPresignedUrl(resolveBucket(bucket), normalizeObjectKey(objectKey), duration);
    }

    /**
     * 批量获取预签名访问地址，用于页面一次预取多张图片，把 N 次签名请求压成 1 次。
     * <p>
     * key 归一化后去重，重复 key 只签名一次；返回顺序与入参顺序一致。
     * 签名是本地纯计算（不请求存储服务），串行执行即可，无需额外并发。
     *
     * @param bucket        桶名，为空时使用默认桶
     * @param objectKeys    桶内对象 key 列表，即上传接口返回的 objectKey
     * @param expireSeconds 地址有效期（秒），为空时默认 30 分钟，最大 7 天
     */
    public List<PresignedUrlVo> getPresignedUrls(String bucket, List<String> objectKeys, Long expireSeconds) {
        if (objectKeys == null || objectKeys.isEmpty()) {
            throw new AppException("objectKeys 不能为空");
        }
        String targetBucket = resolveBucket(bucket);
        Duration duration = resolvePresignDuration(expireSeconds);

        Set<String> normalizedKeys = new LinkedHashSet<>(objectKeys.size());
        for (String objectKey : objectKeys) {
            normalizedKeys.add(normalizeObjectKey(objectKey));
        }

        List<PresignedUrlVo> presignedUrls = new ArrayList<>(normalizedKeys.size());
        for (String objectKey : normalizedKeys) {
            PresignedUrlVo presignedUrlVo = new PresignedUrlVo();
            presignedUrlVo.setBucket(targetBucket);
            presignedUrlVo.setObjectKey(objectKey);
            presignedUrlVo.setUrl(storageAdapter.getPresignedUrl(targetBucket, objectKey, duration));
            presignedUrls.add(presignedUrlVo);
        }
        return presignedUrls;
    }

    /**
     * 对象 key 归一化：去掉首尾空白和前置斜杠，空值直接报错。
     * 桶名由调用方单独传参，key 里不应再带桶名前缀。
     */
    public String normalizeObjectKey(String objectKey) {
        if (!StringUtils.hasText(objectKey)) {
            throw new AppException("objectKey 不能为空");
        }
        String normalized = objectKey.trim();
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (!StringUtils.hasText(normalized)) {
            throw new AppException("objectKey 不能为空");
        }
        return normalized;
    }

    /**
     * 预签名地址有效期，不传时使用默认值，上限为 S3 允许的 7 天
     */
    private Duration resolvePresignDuration(Long expireSeconds) {
        if (expireSeconds == null) {
            return DEFAULT_PRESIGN_DURATION;
        }
        if (expireSeconds <= 0 || expireSeconds > MAX_PRESIGN_DURATION.toSeconds()) {
            throw new AppException("expireSeconds 取值范围 1 ~ " + MAX_PRESIGN_DURATION.toSeconds());
        }
        return Duration.ofSeconds(expireSeconds);
    }

}
