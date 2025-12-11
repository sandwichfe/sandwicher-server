package com.lww.auth.server.user_center.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(description = "菜单请求参数")
public class MenuReq implements Serializable {
    @Schema(description = "菜单自增ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "父菜单ID")
    private Long menuPid;

    @Schema(description = "跳转URL")
    private String path;

    @Schema(description = "所需权限")
    private String authority;

    @Schema(description = "0:菜单,1:接口")
    private Byte type;
}
