package com.lww.auth.server.portal.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 授权码换取token请求参数
 *
 * @author lww
 * @since 2026/06/30
 */
@Data
@Schema(description = "授权码换取token请求")
public class ExchangeTokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 授权码
     */
    @NotBlank(message = "授权码不能为空")
    @Schema(description = "授权码", example = "AUTH_CODE_1234567890_xyz789")
    private String code;

    /**
     * 客户端ID
     */
    @NotBlank(message = "客户端ID不能为空")
    @Schema(description = "客户端ID", example = "littleNote")
    private String clientId;

    /**
     * 回调地址（必须与生成授权码时的地址一致）
     */
    @NotBlank(message = "回调地址不能为空")
    @Schema(description = "回调地址", example = "http://localhost:3000/note")
    private String redirectUri;
}
