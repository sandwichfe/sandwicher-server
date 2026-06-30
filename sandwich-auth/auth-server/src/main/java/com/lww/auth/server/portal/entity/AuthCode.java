package com.lww.auth.server.portal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 授权码实体类（用于OAuth2授权码模式）
 *
 * @author lww
 * @since 2026/06/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("auth_codes")
public class AuthCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 授权码（唯一）
     */
    @TableField("code")
    private String code;

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 回调地址
     */
    @TableField("redirect_uri")
    private String redirectUri;

    /**
     * 状态参数（防CSRF攻击）
     */
    @TableField("state")
    private String state;

    /**
     * 是否已使用（0-未使用，1-已使用）
     */
    @TableField("used")
    private Integer used;

    /**
     * 过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
