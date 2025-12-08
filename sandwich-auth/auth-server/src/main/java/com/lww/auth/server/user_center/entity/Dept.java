package com.lww.auth.server.user_center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author lww
 * @since 2024-12-16
 */
@Getter
@Setter
@TableName("t_dept")
@Schema(name = "Dept", description = "部门表")
public class Dept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    private Boolean status;
}
