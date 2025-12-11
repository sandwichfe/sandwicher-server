package com.lww.auth.server.user_center.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(description = "角色请求参数")
public class RoleReq implements Serializable {
    @Schema(description = "角色自增ID")
    private Long id;

    @Schema(description = "角色名")
    private String roleName;
}
