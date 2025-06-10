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
 * 三方登录账户信息表
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Getter
@Setter
@TableName("t_third_account")
@Schema(name = "ThirdAccount", description = "三方登录账户信息表")
public class ThirdAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户表主键")
    private Long userId;

    @Schema(description = "三方登录唯一id")
    private String uniqueId;

    @Schema(description = "三方登录类型")
    private String type;

    @Schema(description = "博客地址")
    private String blog;

    @Schema(description = "地址")
    private String location;

    @Schema(description = "绑定时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
