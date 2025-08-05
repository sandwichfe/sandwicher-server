package com.lww.auth.server.user.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改密码请求DTO
 *
 * @author lww
 * @since 2024-12-16
 */
@Data
@Schema(name = "ChangePasswordRequest", description = "修改密码请求")
public class ChangePasswordRequest {

    @Schema(description = "原密码")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "新密码长度不能少于6位")
    private String newPassword;

    @Schema(description = "确认新密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}