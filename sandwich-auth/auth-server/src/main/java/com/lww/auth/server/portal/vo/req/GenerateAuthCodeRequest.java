package com.lww.auth.server.portal.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 生成授权码请求参数
 *
 * @author lww
 * @since 2026/06/30
 */
@Data
@Schema(description = "生成授权码请求")
public class GenerateAuthCodeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    @NotBlank(message = "客户端ID不能为空")
    @Schema(description = "客户端ID", example = "littleNote")
    private String clientId;

    /**
     * 用户登录token
     */
    @NotBlank(message = "登录token不能为空")
    @Schema(description = "用户登录token")
    private String loginToken;

    /**
     * 回调地址
     */
    @NotBlank(message = "回调地址不能为空")
    @Schema(description = "回调地址", example = "http://localhost:3000/note")
    private String redirectUri;

    /**
     * 状态参数（防CSRF攻击）
     */
    @NotBlank(message = "state参数不能为空")
    @Schema(description = "状态参数", example = "state_1234567890_abc123")
    private String state;
}
