package com.lww.auth.server.portal.service;

import com.lww.auth.server.portal.vo.req.ExchangeTokenRequest;
import com.lww.auth.server.portal.vo.req.GenerateAuthCodeRequest;
import com.lww.auth.server.portal.vo.resp.AuthCodeResponse;
import com.lww.auth.server.portal.vo.resp.TokenResponse;

/**
 * 授权码登录服务接口
 *
 * @author lww
 * @since 2026/06/30
 */
public interface AuthCodeService {

    /**
     * 生成授权码
     *
     * @param request 生成授权码请求参数
     * @return 授权码响应
     */
    AuthCodeResponse generateAuthCode(GenerateAuthCodeRequest request);

    /**
     * 用授权码换取token
     *
     * @param request 授权码换取token请求参数
     * @return Token响应
     */
    TokenResponse exchangeToken(ExchangeTokenRequest request);
}
