package com.lww.auth.server.user.controller;

import com.lww.auth.server.user.service.QrCodeLoginService;
import com.lww.auth.server.user.vo.req.QrCodeLoginConsentRequest;
import com.lww.auth.server.user.vo.req.QrCodeLoginScanResponse;
import com.lww.auth.server.user.vo.resp.QrCodeLoginFetchResponse;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 二维码登录接口
 *
 * @author lww
 * @since 2024/12/13
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user/qrCode")
public class QrCodeLoginController {

    private final QrCodeLoginService iQrCodeLoginService;

    @GetMapping("/login/generateQrCode")
    public ResponseResult<String> generateQrCode() {
        // pc 生成二维码qrcodeId
        return ResultUtil.success(iQrCodeLoginService.generateQrCode());
    }

    @GetMapping("/login/fetch/{qrCodeId}")
    public ResponseResult<QrCodeLoginFetchResponse> fetch(@PathVariable String qrCodeId) {
        // pc 轮询二维码状态
        return ResultUtil.success(iQrCodeLoginService.fetch(qrCodeId));
    }


    @GetMapping("/scan")
    public ResponseResult<QrCodeLoginScanResponse> scan( String qrCodeId) {
        // app 扫码二维码
        return ResultUtil.success(iQrCodeLoginService.scan(qrCodeId));
    }

    @PostMapping("/consent")
    public ResponseResult<String> consent(@RequestBody QrCodeLoginConsentRequest loginConsent, HttpServletRequest request) {
        // app 确认登录
        iQrCodeLoginService.consent(loginConsent,request);
        return ResultUtil.success();
    }

}

