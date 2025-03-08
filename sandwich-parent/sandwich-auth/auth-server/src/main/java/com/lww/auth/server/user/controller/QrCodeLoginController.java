package com.lww.auth.server.user.controller;

/**
 * 二维码登录接口
 *
 * @author lww
 * @since 2024/12/13
 */

import com.lww.auth.server.user.service.QrCodeLoginService;
import com.lww.auth.server.user.vo.req.QrCodeLoginConsentRequest;
import com.lww.auth.server.user.vo.req.QrCodeLoginScanRequest;
import com.lww.auth.server.user.vo.req.QrCodeLoginScanResponse;
import com.lww.auth.server.user.vo.resp.QrCodeGenerateResponse;
import com.lww.auth.server.user.vo.resp.QrCodeLoginFetchResponse;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/scan")
    public ResponseResult<QrCodeLoginScanResponse> scan(@RequestBody QrCodeLoginScanRequest loginScan) {
        // app 扫码二维码
        return ResultUtil.success(iQrCodeLoginService.scan(loginScan));
    }

    @PostMapping("/consent")
    public ResponseResult<String> consent(@RequestBody QrCodeLoginConsentRequest loginConsent) {

        // app 确认登录
        iQrCodeLoginService.consent(loginConsent);

        return ResultUtil.success();
    }

}

