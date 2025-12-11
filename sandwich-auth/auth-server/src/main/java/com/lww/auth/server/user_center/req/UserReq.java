package com.lww.auth.server.user_center.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "用户请求参数")
public class UserReq implements Serializable {
    @Schema(description = "自增id")
    private Long id;

    @Schema(description = "用户名、昵称")
    private String nickname;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "部门ID列表")
    private List<Long> deptIds;

    @Schema(description = "用户来源")
    private String sourceFrom;
}
