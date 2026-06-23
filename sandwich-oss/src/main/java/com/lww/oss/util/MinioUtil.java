package com.lww.oss.util;

import com.lww.common.web.exception.AppException;
import com.lww.oss.entity.FileInfo;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import io.minio.messages.ListAllMyBucketsResult.Bucket;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MinIO file utility.
 *
 * @author lww
 * @since 2024
 */
@Component
public class MinioUtil {

    private static final long DEFAULT_PART_SIZE = 5L * 1024 * 1024;
    private static final Map<String, String> CONTENT_TYPE_MAP;

    static {
        Map<String, String> contentTypeMap = new HashMap<>();
        contentTypeMap.put("txt", "text/plain");
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");
        contentTypeMap.put("pdf", "application/pdf");
        contentTypeMap.put("xml", "application/xml");
        contentTypeMap.put("html", "text/html");
        contentTypeMap.put("css", "text/css");
        contentTypeMap.put("js", "application/javascript");
        contentTypeMap.put("csv", "text/csv");
        contentTypeMap.put("mp4", "video/mp4");
        contentTypeMap.put("mp3", "audio/mpeg");
        contentTypeMap.put("wav", "audio/wav");
        contentTypeMap.put("zip", "application/zip");
        contentTypeMap.put("tar", "application/x-tar");
        contentTypeMap.put("rar", "application/vnd.rar");
        contentTypeMap.put("bmp", "image/bmp");
        contentTypeMap.put("tiff", "image/tiff");
        contentTypeMap.put("svg", "image/svg+xml");
        contentTypeMap.put("woff", "font/woff");
        contentTypeMap.put("woff2", "font/woff2");
        contentTypeMap.put("otf", "font/otf");
        contentTypeMap.put("eot", "application/vnd.ms-fontobject");
        contentTypeMap.put("ico", "image/x-icon");
        contentTypeMap.put("apk", "application/vnd.android.package-archive");
        contentTypeMap.put("dmg", "application/x-apple-diskimage");
        CONTENT_TYPE_MAP = Collections.unmodifiableMap(contentTypeMap);
    }

    @Resource
    private MinioClient minioClient;

    /**
     * 创建bucket桶
     */
    public void createBucket(String bucket) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (ErrorResponseException exception) {
            String errorCode = exception.errorResponse().code();
            if (!"BucketAlreadyOwnedByYou".equals(errorCode) && !"BucketAlreadyExists".equals(errorCode)) {
                throw buildOperationException("create bucket", bucket, null, exception);
            }
        } catch (Exception exception) {
            throw buildOperationException("create bucket", bucket, null, exception);
        }
    }

    /**
     * 上传文件
     */
    public void uploadFile(InputStream inputStream, String bucket, String objectName, long objectSize, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(inputStream, objectSize, DEFAULT_PART_SIZE)
                    .contentType(resolveContentType(objectName, contentType))
                    .build());
        } catch (Exception exception) {
            throw buildOperationException("upload object", bucket, objectName, exception);
        }
    }

    public List<String> getAllBucket() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets.stream().map(Bucket::name).collect(Collectors.toList());
        } catch (Exception exception) {
            throw buildOperationException("list buckets", null, null, exception);
        }
    }

    /**
     * 列出当前桶及文件
     */
    public List<FileInfo> getAllFile(String bucket) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            List<FileInfo> fileInfoList = new ArrayList<>();
            for (Result<Item> result : results) {
                Item item = result.get();
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(item.objectName());
                fileInfo.setDirectoryFlag(item.isDir());
                fileInfo.setEtag(item.etag());
                fileInfoList.add(fileInfo);
            }
            return fileInfoList;
        } catch (Exception exception) {
            throw buildOperationException("list objects", bucket, null, exception);
        }
    }

    /**
     * 下载文件
     */
    public InputStream download(String bucket, String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception exception) {
            throw buildOperationException("download object", bucket, objectName, exception);
        }
    }

    /**
     * 删除桶
     */
    public void deleteBucket(String bucket) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
        } catch (Exception exception) {
            throw buildOperationException("delete bucket", bucket, null, exception);
        }
    }

    /**
     * 删除文件
     */
    public void deleteObject(String bucket, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception exception) {
            throw buildOperationException("delete object", bucket, objectName, exception);
        }
    }

    /**
     * 获取文件url
     */
    public String getPreviewFileUrl(String bucketName, String objectName) {
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(Http.Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            return minioClient.getPresignedObjectUrl(args);
        } catch (Exception exception) {
            throw buildOperationException("get preview url", bucketName, objectName, exception);
        }
    }

    /**
     * 根据文件扩展名获取 contentType
     *
     * @author lww
     * @since 2024-10-21
     */
    private static String resolveContentType(String filePath, String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType;
        }
        String extension = getFileExtension(filePath);
        return CONTENT_TYPE_MAP.getOrDefault(extension.toLowerCase(Locale.ROOT), "application/octet-stream");
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

    private AppException buildOperationException(String operation, String bucket, String objectName, Exception exception) {
        StringBuilder messageBuilder = new StringBuilder("MinIO ");
        messageBuilder.append(operation).append(" failed");
        if (StringUtils.hasText(bucket)) {
            messageBuilder.append(", bucket=").append(bucket);
        }
        if (StringUtils.hasText(objectName)) {
            messageBuilder.append(", objectName=").append(objectName);
        }
        messageBuilder.append(", reason=").append(exception.getMessage());
        return new AppException(messageBuilder.toString());
    }
}

