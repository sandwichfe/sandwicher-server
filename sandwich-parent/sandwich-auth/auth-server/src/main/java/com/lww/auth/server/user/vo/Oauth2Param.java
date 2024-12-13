package com.lww.auth.server.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Oauth2请求token 参数
 *
 * @author lww
 * @since 2024/12/13
 */
@Data
@Accessors(chain = true)
public class Oauth2Param {

    @Schema(description = "授权类型，通常为 password", example = "password")
    private String grantType;

    @Schema(description = "客户端 ID", example = "client_password")
    private String clientId;

    @Schema(description = "客户端密钥", example = "123456")
    private String clientSecret;

    @Schema(description = "用户名", example = "exampleUser")
    private String username;

    @Schema(description = "用户密码", example = "examplePassword")
    private String password;

}
