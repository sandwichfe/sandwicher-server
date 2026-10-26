package com.lww.oss.adapter;

import com.lww.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * Aliyun OSS adapter.
 *
 * @author lww
 * @since 2023/10/14
 */
public class AliStorageAdapter implements StorageAdapter {

    @Override
    public void createBucket(String bucket) {
    }

    @Override
    public String uploadFile(MultipartFile uploadFile, String bucket, String dir) {
        return null;
    }

    @Override
    public List<String> getAllBucket() {
        List<String> bucketNameList = new LinkedList<>();
        bucketNameList.add("aliyun");
        return bucketNameList;
    }

    @Override
    public List<FileInfo> getAllFile(String bucket) {
        return null;
    }

    @Override
    public InputStream download(String bucket, String objectName) {
        return null;
    }

    @Override
    public void deleteBucket(String bucket) {
    }

    @Override
    public void deleteObject(String bucket, String objectName) {
    }

    /**
     * 阿里云适配器尚未接入，新增能力统一抛出异常，避免调用方拿到 null 继续执行。
     */
    @Override
    public String getPresignedUrl(String bucket, String objectName, Duration duration) {
        throw new UnsupportedOperationException("Ali OSS getPresignedUrl is not implemented yet");
    }

    @Override
    public FileInfo statObject(String bucket, String objectName) {
        throw new UnsupportedOperationException("Ali OSS statObject is not implemented yet");
    }

    @Override
    public boolean objectExists(String bucket, String objectName) {
        throw new UnsupportedOperationException("Ali OSS objectExists is not implemented yet");
    }
}
