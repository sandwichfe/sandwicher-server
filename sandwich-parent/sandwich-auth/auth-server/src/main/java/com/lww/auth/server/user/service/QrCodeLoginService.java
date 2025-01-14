package com.lww.auth.server.user.service;

import com.lww.auth.server.user.vo.req.QrCodeLoginScanRequest;
import com.lww.auth.server.user.vo.req.*;
import com.lww.auth.server.user.vo.resp.*;

/**
 * 二维码登录服务接口
 *
 * @author lww
 * @since 2024/12/13
 */
public interface QrCodeLoginService {

    /**
     * 生成二维码
     *
     * @return 二维码
     */
    String generateQrCode();

    /**
     * 扫描二维码响应
     *
     * @param loginScan 二维码id
     * @return 二维码信息
     */
    QrCodeLoginScanResponse scan(QrCodeLoginScanRequest loginScan);

    /**
     * 二维码登录确认入参
     *
     * @param loginConsent 二维码id
     */
    void consent(QrCodeLoginConsentRequest loginConsent);

    /**
     * web端轮询二维码状态处理
     *
     * @param qrCodeId 二维码id
     * @return 二维码信息
     */
    QrCodeLoginFetchResponse fetch(String qrCodeId);


}
