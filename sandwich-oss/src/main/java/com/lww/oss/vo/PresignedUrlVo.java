package com.lww.oss.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 预签名地址结果。
 * <p>
 * url 带时效，只用于即时访问，不要入库；需要长期保存的仍是 objectKey。
 *
 * @author lww
 * @since 2026-10-26
 */
@Data
public class PresignedUrlVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "桶名")
    private String bucket;

    @Schema(description = "桶内对象 key")
    private String objectKey;

    @Schema(description = "预签名访问地址，仅限有效期内使用，不要入库")
    private String url;
}
