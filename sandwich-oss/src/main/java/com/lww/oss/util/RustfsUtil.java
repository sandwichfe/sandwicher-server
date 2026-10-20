package com.lww.oss.util;

import com.lww.web.support.exception.AppException;
import com.lww.oss.entity.FileInfo;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RustFS file utility，保留文件操作接口和 contentType 推断规则。
 *
 * @author lww
 * @since 2024
 */
@Component
@ConditionalOnProperty(prefix = "oss.service", name = "type", havingValue = "rustfs")
public class RustfsUtil {

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
    private S3Client rustfsClient;

    @Resource
    private S3Presigner rustfsPresigner;

    /**
     * 创建bucket桶
     */
    public void createBucket(String bucket) {
        try {
            try {
                rustfsClient.headBucket(request -> request.bucket(bucket));
                return;
            } catch (S3Exception exception) {
                // 仅桶不存在时尝试创建，权限错误和连接异常不能当作不存在。
                if (exception.statusCode() != 404) {
                    throw exception;
                }
            }
            rustfsClient.createBucket(request -> request.bucket(bucket));
        } catch (S3Exception exception) {
            String errorCode = getErrorCode(exception);
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
            rustfsClient.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectName)
                    .contentType(resolveContentType(objectName, contentType))
                    .build(), RequestBody.fromInputStream(inputStream, objectSize));
        } catch (Exception exception) {
            throw buildOperationException("upload object", bucket, objectName, exception);
        }
    }

    /**
     * 列出当前账户可访问的全部桶。
     */
    public List<String> getAllBucket() {
        try {
            return rustfsClient.listBucketsPaginator().buckets().stream()
                    .map(Bucket::name).collect(Collectors.toList());
        } catch (Exception exception) {
            throw buildOperationException("list buckets", null, null, exception);
        }
    }

    /**
     * 判断对象是否已存在；调用前应确保桶存在，权限、连接等异常继续抛出。
     *
     * @author lww
     * @since 2026-10-20
     */
    public boolean objectExists(String bucket, String objectName) {
        try {
            rustfsClient.headObject(request -> request.bucket(bucket).key(objectName));
            return true;
        } catch (S3Exception exception) {
            // HEAD 请求可能仅返回 HTTP 404，对象不存在的判断统一封装。
            if (isObjectNotFound(exception)) {
                return false;
            }
            throw buildOperationException("stat object", bucket, objectName, exception);
        } catch (Exception exception) {
            throw buildOperationException("stat object", bucket, objectName, exception);
        }
    }

    /**
     * 列出当前桶及文件，分页读取并通过分隔符保留原有的一级目录展示方式。
     */
    public List<FileInfo> getAllFile(String bucket) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucket).delimiter("/").build();
            List<FileInfo> fileInfoList = new ArrayList<>();
            for (ListObjectsV2Response response : rustfsClient.listObjectsV2Paginator(request)) {
                for (S3Object object : response.contents()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(object.key());
                    fileInfo.setDirectoryFlag(false);
                    fileInfo.setEtag(object.eTag() == null ? null : object.eTag().replace("\"", ""));
                    fileInfoList.add(fileInfo);
                }
                for (CommonPrefix prefix : response.commonPrefixes()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(prefix.prefix());
                    fileInfo.setDirectoryFlag(true);
                    fileInfoList.add(fileInfo);
                }
            }
            return fileInfoList;
        } catch (Exception exception) {
            throw buildOperationException("list objects", bucket, null, exception);
        }
    }

    /**
     * 下载文件，返回的响应流由调用方关闭。
     */
    public InputStream download(String bucket, String objectName) {
        try {
            return rustfsClient.getObject(request -> request.bucket(bucket).key(objectName));
        } catch (Exception exception) {
            throw buildOperationException("download object", bucket, objectName, exception);
        }
    }

    /**
     * 删除桶
     */
    public void deleteBucket(String bucket) {
        try {
            rustfsClient.deleteBucket(request -> request.bucket(bucket));
        } catch (Exception exception) {
            throw buildOperationException("delete bucket", bucket, null, exception);
        }
    }

    /**
     * 删除文件
     */
    public void deleteObject(String bucket, String objectName) {
        try {
            rustfsClient.deleteObject(request -> request.bucket(bucket).key(objectName));
        } catch (Exception exception) {
            throw buildOperationException("delete object", bucket, objectName, exception);
        }
    }

    /**
     * 获取文件url，使用有效期为 7 天的 GET 预签名地址访问私有对象。
     */
    public String getPreviewFileUrl(String bucketName, String objectName) {
        try {
            GetObjectPresignRequest request = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(7))
                    .getObjectRequest(GetObjectRequest.builder().bucket(bucketName).key(objectName).build())
                    .build();
            return rustfsPresigner.presignGetObject(request).url().toString();
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

    /**
     * 仅将 HTTP 404 视为对象不存在，明确的桶不存在错误仍保留为异常。
     * 无错误详情的 HEAD 响应无法区分桶和对象不存在，因此调用方需先确保桶存在。
     */
    private static boolean isObjectNotFound(S3Exception exception) {
        return exception.statusCode() == 404 && !"NoSuchBucket".equals(getErrorCode(exception));
    }

    /**
     * HEAD 错误可能没有响应体，读取错误码前先判断错误详情是否存在。
     */
    private static String getErrorCode(S3Exception exception) {
        return exception.awsErrorDetails() == null ? null : exception.awsErrorDetails().errorCode();
    }

    /**
     * 将 SDK 异常转换为业务异常，并保留操作、桶及对象信息。
     */
    private AppException buildOperationException(String operation, String bucket, String objectName, Exception exception) {
        StringBuilder messageBuilder = new StringBuilder("RustFS ");
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

