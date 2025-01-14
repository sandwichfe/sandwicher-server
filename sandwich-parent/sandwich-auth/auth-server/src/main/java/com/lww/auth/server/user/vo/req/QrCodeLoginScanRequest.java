package com.lww.auth.server.user.vo.req;

/**
 * todo
 *
 * @author lww
 * @since 2024/12/13
 */

import lombok.Data;

/**
 * 扫描二维码入参
 *
 * @author vains
 */
@Data
public class QrCodeLoginScanRequest {

    /**
     * 二维码id
     */
    private String qrCodeId;

}

