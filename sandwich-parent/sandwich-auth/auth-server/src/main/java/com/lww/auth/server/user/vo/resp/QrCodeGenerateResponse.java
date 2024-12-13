package com.lww.auth.server.user.vo.resp;

/**
 * todo
 *
 * @author lww
 * @since 2024/12/13
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生成二维码响应
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeGenerateResponse {

    /**
     * 二维码id
     */
    private String qrCodeId;

    /**
     * 二维码base64值(这里响应一个链接好一些)
     */
    private String imageData;

}
