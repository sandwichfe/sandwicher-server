package com.lww.auth.server.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单多对多关联表
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Getter
@Setter
@TableName("t_role_menu")
@Schema(name = "RoleMenu", description = "角色菜单多对多关联表")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色菜单关联表自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "权限菜单ID")
    private Long menuId;
}
