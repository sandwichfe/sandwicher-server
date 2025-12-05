package com.lww.auth.server.user_center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户部门关联表
 * </p>
 *
 * @author lww
 * @since 2024-12-16
 */
@Data
@TableName("t_user_dept")
@Schema(name = "UserDept", description = "用户部门关联表")
public class UserDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;
}
