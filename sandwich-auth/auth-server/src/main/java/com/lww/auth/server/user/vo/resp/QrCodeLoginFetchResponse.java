package com.lww.auth.server.user.vo.resp;

/**
 * QrCodeLoginFetchResponse
 *
 * @author lww
 * @since 2024/12/13
 */

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * web前端轮询二维码状态出参
 *
 * @author vains
 */
@Accessors(chain = true)
@Data
public class QrCodeLoginFetchResponse {

    /**
     * 二维码状态
     * -1:无效或已过期 0:待扫描，1:已扫描，2:已确认
     */
    private Integer qrCodeStatus;

    /**
     * 是否已过期
     */
    private Boolean expired;

    /**
     * 扫描人头像
     */
    private String avatarUrl;

    /**
     * 扫描人昵称
     */
    private String name;

    /**
     * 待确认scope
     */
    private Set<String> scopes;

    private String token;

}
