package com.lww.auth.server.user.vo.req;

/**
 * todo
 *
 * @author lww
 * @since 2024/12/13
 */

import lombok.Data;

/**
 * 二维码登录确认入参
 *
 * @author vains
 */
@Data
public class QrCodeLoginConsentRequest {

    /**
     * 二维码id
     */
    private String qrCodeId;

    /**
     * 扫码二维码后产生的临时票据(仅一次有效)
     */
    private String qrCodeTicket;

}

