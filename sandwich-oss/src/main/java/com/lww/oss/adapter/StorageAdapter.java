package com.lww.oss.adapter;

import com.lww.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;

/**
 * File storage adapter.
 *
 * @author lww
 * @since 2023/10/14
 */
public interface StorageAdapter {

    void createBucket(String bucket);

    /**
     * 获取未指定桶时使用的默认桶，业务侧拼接或校验对象路径时使用
     */
    String getDefaultBucket();

    /**
     * 上传文件
     *
     * @param dir 业务目录，例如 avatar、contract；为空时按日期直接归档
     * @return 桶内对象 key（不含桶名、域名和前导斜杠），例如 2026/10/26/xxx.png，由业务侧入库保存，不要存访问地址
     */
    String uploadFile(MultipartFile uploadFile, String bucket, String dir);

    List<String> getAllBucket();

    List<FileInfo> getAllFile(String bucket);

    InputStream download(String bucket, String objectName);

    void deleteBucket(String bucket);

    void deleteObject(String bucket, String objectName);

    /**
     * 获取预签名访问地址，有效期由调用方按业务场景指定，私有桶统一走此方式访问
     *
     * @param duration 地址有效期，不同存储服务对上限的要求不同
     */
    String getPresignedUrl(String bucket, String objectName, Duration duration);

    /**
     * 获取对象元数据，对象不存在时返回 null
     */
    FileInfo statObject(String bucket, String objectName);

    /**
     * 判断对象是否存在，上传查重、幂等校验等场景使用
     */
    boolean objectExists(String bucket, String objectName);
}
