package com.lww.auth.server.user.vo;

/**
 * QrCodeInfo
 *
 * @author lww
 * @since 2024/12/13
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 二维码信息
 *
 * @author vains
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeInfo {

    /**
     * 二维码id
     */
    private String qrCodeId;

    /**
     * 二维码状态
     * 0:待扫描，1:已扫描，2:已确认
     */
    private Integer qrCodeStatus;

    /**
     * 二维码过期时间
     */
    private LocalDateTime expiresTime;

    /**
     * 扫描人头像
     */
    private String avatarUrl;

    /**
     * 扫描人昵称
     */
    private String name;

    /**
     * 待确认的scope
     */
    private Set<String> scopes;

    /**
     * 跳转登录之前请求的接口
     */
    private String beforeLoginRequestUri;

    /**
     * 跳转登录之前请求参数
     */
    private String beforeLoginQueryString;

}

