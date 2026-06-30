package com.lww.auth.server.portal.controller.user;

import com.lww.auth.server.portal.config.api.ApiPortalRestController;
import com.lww.auth.server.portal.service.AuthCodeService;
import com.lww.auth.server.portal.vo.req.ExchangeTokenRequest;
import com.lww.auth.server.portal.vo.req.GenerateAuthCodeRequest;
import com.lww.auth.server.portal.vo.resp.AuthCodeResponse;
import com.lww.auth.server.portal.vo.resp.TokenResponse;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 授权码登录Controller
 * 实现OAuth2 Authorization Code Flow
 *
 * @author lww
 * @since 2026/06/30
 */
@ApiPortalRestController
@RequestMapping("/auth")
@Tag(name = "授权码登录", description = "OAuth2授权码模式登录接口")
public class AuthCodeController {

    @Resource
    private AuthCodeService authCodeService;

    /**
     * 生成授权码
     * Portal登录成功后调用此接口生成授权码
     */
    @PostMapping("/code")
    @Operation(summary = "生成授权码", description = "Portal登录成功后生成一次性授权码，用于子应用换取token")
    @Loggable(module = "auth", type = "generateAuthCode", description = "生成授权码")
    public ResponseResult<AuthCodeResponse> generateAuthCode(@Valid @RequestBody GenerateAuthCodeRequest request) {
        AuthCodeResponse response = authCodeService.generateAuthCode(request);
        return ResultUtil.success(response);
    }

    /**
     * 用授权码换取token
     * 子应用拿到授权码后调用此接口换取正式token
     */
    @PostMapping("/exchange")
    @Operation(summary = "授权码换取token", description = "子应用用一次性授权码换取正式登录token")
    @Loggable(module = "auth", type = "exchangeToken", description = "授权码换取token")
    public ResponseResult<TokenResponse> exchangeToken(@Valid @RequestBody ExchangeTokenRequest request) {
        TokenResponse response = authCodeService.exchangeToken(request);
        return ResultUtil.success(response);
    }
}
