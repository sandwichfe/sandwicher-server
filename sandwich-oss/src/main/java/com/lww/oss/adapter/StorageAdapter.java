package com.lww.oss.adapter;

import com.lww.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * File storage adapter.
 *
 * @author lww
 * @since 2023/10/14
 */
public interface StorageAdapter {

    void createBucket(String bucket);

    String uploadFile(MultipartFile uploadFile, String bucket, String objectName);

    List<String> getAllBucket();

    List<FileInfo> getAllFile(String bucket);

    InputStream download(String bucket, String objectName);

    void deleteBucket(String bucket);

    void deleteObject(String bucket, String objectName);

    String getUrl(String bucket, String objectName);
}
