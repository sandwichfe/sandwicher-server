package com.lww.oss.util;


import com.lww.oss.entity.FileInfo;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * minio文件操作工具
 *
 * @author: lww
 * @since: 2024
 */
@Component
public class MinioUtil {

    /** Map to hold file extensions and their corresponding content types*/
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();

    static {
        CONTENT_TYPE_MAP.put("txt", "text/plain");
        CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP.put("png", "image/png");
        CONTENT_TYPE_MAP.put("gif", "image/gif");
        CONTENT_TYPE_MAP.put("pdf", "application/pdf");
        CONTENT_TYPE_MAP.put("xml", "application/xml");
        CONTENT_TYPE_MAP.put("html", "text/html");
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "application/javascript");
        CONTENT_TYPE_MAP.put("csv", "text/csv");
        CONTENT_TYPE_MAP.put("mp4", "video/mp4");
        CONTENT_TYPE_MAP.put("mp3", "audio/mpeg");
        CONTENT_TYPE_MAP.put("wav", "audio/wav");
        CONTENT_TYPE_MAP.put("zip", "application/zip");
        CONTENT_TYPE_MAP.put("tar", "application/x-tar");
        CONTENT_TYPE_MAP.put("rar", "application/vnd.rar");
        CONTENT_TYPE_MAP.put("bmp", "image/bmp");
        CONTENT_TYPE_MAP.put("tiff", "image/tiff");
        CONTENT_TYPE_MAP.put("svg", "image/svg+xml");
        CONTENT_TYPE_MAP.put("woff", "font/woff");
        CONTENT_TYPE_MAP.put("woff2", "font/woff2");
        CONTENT_TYPE_MAP.put("otf", "font/otf");
        CONTENT_TYPE_MAP.put("eot", "application/vnd.ms-fontobject");
        CONTENT_TYPE_MAP.put("ico", "image/x-icon");
        CONTENT_TYPE_MAP.put("exe", "application/octet-stream");
        CONTENT_TYPE_MAP.put("bin", "application/octet-stream");
        CONTENT_TYPE_MAP.put("apk", "application/vnd.android.package-archive");
        CONTENT_TYPE_MAP.put("dmg", "application/x-apple-diskimage");
        CONTENT_TYPE_MAP.put("iso", "application/octet-stream");
        // 其他类型可以根据需要添加
    }

    @Resource
    private MinioClient minioClient;

    /**
     * 创建bucket桶
     */
    public void createBucket(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    /**
     * 上传文件
     */
    public void uploadFile(InputStream inputStream, String bucket, String objectName) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
                .stream(inputStream, -1, 5242889L).contentType(getContentType(objectName)).build());
    }

    /**
     * 列出所有桶
     */
    public List<String> getAllBucket() throws Exception {
        List<Bucket> buckets = minioClient.listBuckets();
        return buckets.stream().map(Bucket::name).collect(Collectors.toList());
    }

    /**
     * 列出当前桶及文件
     */
    public List<FileInfo> getAllFile(String bucket) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucket).build());
        List<FileInfo> fileInfoList = new LinkedList<>();
        for (Result<Item> result : results) {
            FileInfo fileInfo = new FileInfo();
            Item item = result.get();
            fileInfo.setFileName(item.objectName());
            fileInfo.setDirectoryFlag(item.isDir());
            fileInfo.setEtag(item.etag());
            fileInfoList.add(fileInfo);
        }
        return fileInfoList;
    }

    /**
     * 下载文件
     */
    public InputStream downLoad(String bucket, String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(objectName).build()
        );
    }

    /**
     * 删除桶
     */
    public void deleteBucket(String bucket) throws Exception {
        minioClient.removeBucket(
                RemoveBucketArgs.builder().bucket(bucket).build()
        );
    }

    /**
     * 删除文件
     */
    public void deleteObject(String bucket, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucket).object(objectName).build()
        );
    }

    /**
     * 获取文件url
     */
    public String getPreviewFileUrl(String bucketName, String objectName) throws Exception{
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName).object(objectName).build();
        return minioClient.getPresignedObjectUrl(args);
    }


    /**
     * 根据文件扩展名获取 contentType
     *
     * @author lww
     * @since 2024-10-21
     */
    private static String getContentType(String filePath) {
        String extension = getFileExtension(filePath);
        return CONTENT_TYPE_MAP.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }

    /**
     *获取文件扩展名
     *
     * @author lww
     * @since 2024-10-21
     */
    private static String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            return filePath.substring(dotIndex + 1);
        }
        return "";
    }


}

