package com.lww.auth.server.user_center.entity;

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
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    private Boolean status;

    @Schema(description = "删除标志（0代表存在 1代表删除）")
    private Boolean deleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private Long createUserId;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private Long updateUserId;
}
