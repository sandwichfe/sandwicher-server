package com.lww.oss.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 上传结果。
 * <p>
 * objectKey 是入库用的对象标识（不含桶名、域名和前导斜杠）；
 * url 只是拼好的站内访问地址，方便上传后立即回显，不要存库——换域名、换桶或接口前缀调整都会让它失效。
 *
 * @author lww
 * @since 2026-10-26
 */
@Data
public class UploadVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "桶名")
    private String bucket;

    @Schema(description = "桶内对象 key，入库存这个，例如 2026/10/26/xxx.png")
    private String objectKey;

    @Schema(description = "文件大小，单位字节")
    private Long size;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "站内访问地址，仅用于即时回显，不要入库")
    private String url;
}
