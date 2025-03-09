package com.lww.auth.server.user.vo.req;

/**
 * todo
 *
 * @author lww
 * @since 2024/12/13
 */

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 扫描二维码响应bean
 *
 * @author vains
 */
@Accessors(chain = true)
@Data
public class QrCodeLoginScanResponse {

    /**
     * 扫描临时票据
     */
    private String qrCodeTicket;

    /**
     * 二维码状态
     */
    private Integer qrCodeStatus;

    /**
     * 是否已过期
     */
    private Boolean expired;

    /**
     * 待确认scope
     */
    private Set<String> scopes;

}
