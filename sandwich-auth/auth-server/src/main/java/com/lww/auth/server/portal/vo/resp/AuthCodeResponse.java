package com.lww.auth.server.portal.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 授权码响应
 *
 * @author lww
 * @since 2026/06/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "授权码响应")
public class AuthCodeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 授权码
     */
    @Schema(description = "授权码", example = "AUTH_CODE_1234567890_xyz789")
    private String code;
}
