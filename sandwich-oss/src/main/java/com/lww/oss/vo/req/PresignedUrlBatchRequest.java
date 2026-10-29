package com.lww.oss.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量获取预签名地址请求。
 * <p>
 * 页面展示多张私有桶图片时，一次请求完成全部签名，避免前端按图片数量发起 N 次请求。
 *
 * @author lww
 * @since 2026-10-26
 */
@Data
@Schema(description = "批量获取预签名地址请求")
public class PresignedUrlBatchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单次批量签名上限，防止异常调用把签名接口当成批量任务使用
     */
    public static final int MAX_BATCH_SIZE = 200;

    @Schema(description = "桶名，为空时使用默认桶")
    private String bucket;

    @NotEmpty(message = "objectKeys 不能为空")
    @Size(max = MAX_BATCH_SIZE, message = "单次最多批量签名 " + MAX_BATCH_SIZE + " 个对象")
    @Schema(description = "桶内对象 key 列表，即上传接口返回的 objectKey；重复 key 只签名一次",
            example = "[\"2026/10/26/a.png\",\"2026/10/26/b.png\"]")
    private List<String> objectKeys;

    @Schema(description = "地址有效期（秒），默认 30 分钟，最大 7 天")
    private Long expireSeconds;
}
