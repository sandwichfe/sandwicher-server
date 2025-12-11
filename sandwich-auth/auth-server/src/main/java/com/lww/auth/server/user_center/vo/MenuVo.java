package com.lww.auth.server.user_center.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Schema(description = "菜单VO")
public class MenuVo implements Serializable {
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
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
