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
 * 基础用户信息表
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@Getter
@Setter
@TableName("t_user")
@Schema(name = "User", description = "基础用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @Schema(description = "是否已删除")
    private Boolean deleted;

    @Schema(description = "用户来源")
    private String sourceFrom;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
