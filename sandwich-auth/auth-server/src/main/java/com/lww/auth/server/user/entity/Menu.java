package com.lww.auth.server.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Getter
@Setter
@TableName("t_menu")
@Schema(name = "menu", description = "系统菜单表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "父菜单ID")
    private Long menuPid;

    @Schema(description = "跳转URL")
    private String path;

    @Schema(description = "所需权限")
    private String authority;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "0:菜单,1:接口")
    private Byte type;

    @Schema(description = "0:启用,1:删除")
    private Boolean deleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createUserId;
}
