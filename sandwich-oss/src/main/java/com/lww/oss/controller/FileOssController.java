package com.lww.oss.controller;


import com.lww.web.support.exception.AppException;
import com.lww.web.support.response.ResponseResult;
import com.lww.web.support.response.ResultUtil;
import com.lww.oss.enums.OssBizType;
import com.lww.oss.service.FileOssService;
import com.lww.oss.vo.PresignedUrlVo;
import com.lww.oss.vo.UploadVo;
import com.lww.oss.vo.req.PresignedUrlBatchRequest;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * 文件操作controller
 *
 * @author lww
 * @since 2023/10/14
 */
@RestController
@RequestMapping("/api/oss")
public class FileOssController {

    /**
     * 跳转地址有效期，需大于浏览器缓存 302 的时长，避免命中缓存时签名已过期
     */
    private static final Duration REDIRECT_PRESIGN_DURATION = Duration.ofHours(1);

    /**
     * 302 缓存时长，浏览器在该时长内复用跳转结果，不再重复请求后端
     */
    private static final Duration REDIRECT_CACHE_DURATION = Duration.ofMinutes(30);

    /**
     * 对象访问路径中的固定前缀，用于从请求路径中截取对象 key
     */
    private static final String OBJECT_PATH_SEGMENT = "file/";

    /**
     * 对象访问接口前缀，与 {@link #accessObject} 的映射路径保持一致，用于拼装回显地址
     */
    private static final String OBJECT_ACCESS_URL_PREFIX = "/api/oss/file/";

    @Resource
    private FileOssService fileService;

    /**
     * 上传文件。
     * <p>
     * 返回 objectKey（入库存这个）和 url（仅用于上传后立即回显，不入库）。
     *
     * @param bizType 业务类型，目录由服务端按枚举决定，例如 note-image、avatar、attachment
     * @param dir     业务目录，仅在未传 bizType 时作为兼容参数使用；为空时按日期归档
     */
    @PostMapping("/upload")
    public ResponseResult<UploadVo> upload(@RequestPart("uploadFile") MultipartFile uploadFile,
                                           String bucket,
                                           String bizType,
                                           String dir) {
        String targetBucket = fileService.resolveBucket(bucket);
        String objectKey = fileService.uploadFile(uploadFile, targetBucket, resolveUploadDir(bizType, dir));

        UploadVo uploadVo = new UploadVo();
        uploadVo.setBucket(targetBucket);
        uploadVo.setObjectKey(objectKey);
        uploadVo.setSize(uploadFile.getSize());
        uploadVo.setContentType(uploadFile.getContentType());
        uploadVo.setUrl(buildAccessUrl(targetBucket, objectKey));
        return ResultUtil.success(uploadVo);
    }

    /**
     * 获取对象的预签名访问地址。
     * 适用于前端需要拿到直连地址自行使用的场景（如导出、下载、第三方预览），
     * 页面内直接展示建议使用 {@link #accessObject(String, HttpServletRequest)}，地址长期稳定。
     *
     * @param bucket        桶名，为空时使用默认桶
     * @param objectKey     桶内对象 key，不含桶名和域名，即上传接口返回的 objectKey
     * @param expireSeconds 地址有效期（秒），默认 30 分钟，最大 7 天
     */
    @GetMapping("/presigned-url")
    public ResponseResult<String> getPresignedUrl(String bucket,
                                                  @RequestParam("objectKey") String objectKey,
                                                  @RequestParam(required = false) Long expireSeconds) {
        return ResultUtil.success(fileService.getPresignedUrl(bucket, objectKey, expireSeconds));
    }

    /**
     * 批量获取对象的预签名访问地址。
     * <p>
     * 图片、附件较多的页面（如笔记详情、仪表盘）建议在拿到业务数据后一次性预取，把 N 次签名请求压成 1 次；
     * 返回顺序与请求顺序一致，重复 key 只签名一次。需要长期稳定的地址仍用 {@link #accessObject}。
     *
     * @param request 批量签名参数，objectKeys 必传且单次不超过 {@link PresignedUrlBatchRequest#MAX_BATCH_SIZE} 个
     */
    @PostMapping("/presigned-urls")
    public ResponseResult<List<PresignedUrlVo>> getPresignedUrls(@Valid @RequestBody PresignedUrlBatchRequest request) {
        List<PresignedUrlVo> presignedUrls = fileService.getPresignedUrls(
                request.getBucket(), request.getObjectKeys(), request.getExpireSeconds());
        return ResultUtil.success(presignedUrls);
    }

    /**
     * 按桶名 + 对象 key 访问文件，302 跳转到预签名地址后由存储服务直接返回文件内容。
     * <p>
     * 该地址路径与桶内对象结构一致，可长期入库保存，例如：
     * https://{域名}/api/oss/file/yiru/2026/10/26/image.png
     * <p>
     * 前端可直接用于 img/video 标签，浏览器无法携带 token，需在 security.permit.urls 中放行。
     */
    @GetMapping("/file/{bucket}/**")
    public ResponseEntity<Void> accessObject(@PathVariable String bucket, HttpServletRequest request) {
        String objectKey = resolveObjectKey(request, bucket);
        String presignedUrl = fileService.getPresignedUrl(bucket, objectKey, REDIRECT_PRESIGN_DURATION);
        return ResponseEntity.status(HttpStatus.FOUND)
                .cacheControl(CacheControl.maxAge(REDIRECT_CACHE_DURATION).cachePrivate())
                .location(URI.create(presignedUrl))
                .build();
    }

    /**
     * 解析上传目录：优先使用 bizType 枚举（目录由服务端决定），未传时兼容旧的 dir 参数。
     */
    private String resolveUploadDir(String bizType, String dir) {
        if (!StringUtils.hasText(bizType)) {
            return dir;
        }
        OssBizType type = OssBizType.of(bizType);
        if (type == null) {
            throw new AppException("不支持的 bizType：" + bizType + "，可选值：" + OssBizType.codes());
        }
        return type.getDir();
    }

    /**
     * 拼装站内访问地址，仅供上传后立即回显，不要入库
     */
    private String buildAccessUrl(String bucket, String objectKey) {
        String key = objectKey.startsWith("/") ? objectKey.substring(1) : objectKey;
        return OBJECT_ACCESS_URL_PREFIX + bucket + "/" + UriUtils.encodePath(key, StandardCharsets.UTF_8);
    }

    /**
     * 从请求路径中截取桶名之后的对象 key，保留目录层级。
     * 使用 handlerMapping 记录的匹配路径而非 requestURI，避免 context-path 和编码差异影响 key。
     */
    private String resolveObjectKey(HttpServletRequest request, String bucket) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (!StringUtils.hasText(path)) {
            path = request.getRequestURI();
        }
        String prefix = OBJECT_PATH_SEGMENT + bucket + "/";
        int prefixIndex = path.indexOf(prefix);
        if (prefixIndex < 0) {
            throw new AppException("非法的对象访问路径：" + path);
        }
        return path.substring(prefixIndex + prefix.length());
    }

}
