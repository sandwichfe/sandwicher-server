package com.lww.auth.server.portal.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Token响应
 *
 * @author lww
 * @since 2026/06/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Token响应")
public class TokenResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问token
     */
    @Schema(description = "访问token")
    private String token;

    /**
     * 过期时间（秒）
     */
    @Schema(description = "过期时间（秒）", example = "604800")
    private Long expiresIn;
}
