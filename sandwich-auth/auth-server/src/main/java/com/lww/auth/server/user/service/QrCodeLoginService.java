package com.lww.auth.server.user.service;

import com.lww.auth.server.user.vo.req.*;
import com.lww.auth.server.user.vo.resp.*;
import jakarta.servlet.http.HttpServletRequest;

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
     * web端轮询二维码状态处理
     *
     * @param qrCodeId 二维码id
     * @return 二维码信息
     */
    QrCodeLoginFetchResponse fetch(String qrCodeId);

    /**
     * 扫描二维码响应
     *
     * @param  qrCodeId 二维码id
     * @return 二维码信息
     */
    QrCodeLoginScanResponse scan(String qrCodeId);

    /**
     * 二维码登录确认入参
     *
     * @param loginConsent 二维码id
     */
    void consent(QrCodeLoginConsentRequest loginConsent, HttpServletRequest request);




}
