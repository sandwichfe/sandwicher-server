package com.lww.auth.server.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.lww.auth.server.portal.config.AuthClientProperties;
import com.lww.auth.server.portal.service.AuthCodeService;
import com.lww.auth.server.portal.service.UserService;
import com.lww.auth.server.portal.vo.req.ExchangeTokenRequest;
import com.lww.auth.server.portal.vo.req.GenerateAuthCodeRequest;
import com.lww.auth.server.portal.vo.resp.AuthCodeResponse;
import com.lww.auth.server.portal.vo.resp.TokenResponse;
import com.lww.common.web.exception.AppException;
import com.lww.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 授权码登录服务实现类
 * 使用Redis存储授权码，实现OAuth2 Authorization Code Flow
 *
 * @author lww
 * @since 2026/06/30
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthCodeServiceImpl implements AuthCodeService {

    private final RedisUtil redisUtil;
    private final AuthClientProperties authClientProperties;

    /**
     * 授权码过期时间（秒）- 5分钟
     */
    private static final long AUTH_CODE_TIMEOUT = 5 * 60;

    /**
     * Token过期时间（秒）- 7天
     */
    private static final long TOKEN_EXPIRES_IN = 7 * 24 * 60 * 60;

    /**
     * 授权码Redis前缀
     */
    private static final String AUTH_CODE_PREFIX = "auth:code:";

    @Override
    public AuthCodeResponse generateAuthCode(GenerateAuthCodeRequest request) {
        log.info("开始生成授权码，clientId: {}, redirectUri: {}", request.getClientId(), request.getRedirectUri());

        // 1. 验证客户端是否在白名单中
        validateClient(request.getClientId(), request.getRedirectUri());

        // 2. 验证loginToken是否有效（简单检查非空，实际应解析JWT验证）
        if (StringUtils.isBlank(request.getLoginToken())) {
            throw new AppException("登录token无效");
        }

        // 3. 生成唯一授权码
        String authCode = "AUTH_CODE_" + IdWorker.getIdStr();

        // 4. 构建授权码信息并存储到Redis
        AuthCodeInfo authCodeInfo = AuthCodeInfo.builder()
                .code(authCode)
                .clientId(request.getClientId())
                .loginToken(request.getLoginToken())
                .redirectUri(request.getRedirectUri())
                .state(request.getState())
                .used(false)
                .expireTime(LocalDateTime.now().plusSeconds(AUTH_CODE_TIMEOUT))
                .build();

        redisUtil.set(AUTH_CODE_PREFIX + authCode, authCodeInfo, AUTH_CODE_TIMEOUT);

        log.info("授权码生成成功: {}", authCode);
        return AuthCodeResponse.builder().code(authCode).build();
    }

    @Override
    public TokenResponse exchangeToken(ExchangeTokenRequest request) {
        log.info("开始用授权码换取token，code: {}, clientId: {}", request.getCode(), request.getClientId());

        // 1. 从Redis获取授权码信息
        AuthCodeInfo authCodeInfo = redisUtil.get(AUTH_CODE_PREFIX + request.getCode());
        if (authCodeInfo == null) {
            log.warn("授权码不存在或已过期: {}", request.getCode());
            throw new AppException("授权码无效或已过期");
        }

        // 2. 验证授权码是否已使用
        if (authCodeInfo.getUsed()) {
            log.warn("授权码已被使用: {}", request.getCode());
            throw new AppException("授权码已被使用");
        }

        // 3. 验证授权码是否过期
        if (authCodeInfo.getExpireTime().isBefore(LocalDateTime.now())) {
            log.warn("授权码已过期: {}, 过期时间: {}", request.getCode(), authCodeInfo.getExpireTime());
            redisUtil.del(AUTH_CODE_PREFIX + request.getCode());
            throw new AppException("授权码已过期");
        }

        // 4. 验证客户端ID是否匹配
        if (!Objects.equals(authCodeInfo.getClientId(), request.getClientId())) {
            log.warn("客户端ID不匹配，授权码绑定的clientId: {}, 请求的clientId: {}",
                    authCodeInfo.getClientId(), request.getClientId());
            throw new AppException("客户端验证失败");
        }

        // 5. 验证回调地址是否匹配（防止授权码被盗用）
        if (!Objects.equals(authCodeInfo.getRedirectUri(), request.getRedirectUri())) {
            log.warn("回调地址不匹配，授权码绑定的redirectUri: {}, 请求的redirectUri: {}",
                    authCodeInfo.getRedirectUri(), request.getRedirectUri());
            throw new AppException("回调地址验证失败");
        }

        // 6. 标记授权码为已使用（防止重放攻击）
        authCodeInfo.setUsed(true);
        redisUtil.set(AUTH_CODE_PREFIX + request.getCode(), authCodeInfo, 60); // 保留1分钟用于日志追踪

        // 7. 使用原始的loginToken作为返回的token
        String token = authCodeInfo.getLoginToken();

        log.info("授权码换取token成功，clientId: {}", request.getClientId());

        // 8. 删除授权码（可选，也可以保留一段时间用于日志审计）
        // redisUtil.del(AUTH_CODE_PREFIX + request.getCode());

        return TokenResponse.builder()
                .token(token)
                .expiresIn(TOKEN_EXPIRES_IN)
                .build();
    }

    /**
     * 验证客户端ID和回调地址是否在白名单中
     *
     * @param clientId    客户端ID
     * @param redirectUri 回调地址
     */
    private void validateClient(String clientId, String redirectUri) {
        // 从配置中查找匹配的客户端
        AuthClientProperties.ClientInfo clientInfo = authClientProperties.getWhitelist().stream()
                .filter(client -> clientId.equals(client.getClientId()))
                .findFirst()
                .orElse(null);

        // 检查客户端是否存在
        if (clientInfo == null) {
            log.warn("客户端不在白名单中: {}", clientId);
            throw new AppException("客户端未注册");
        }

        List<String> allowedUris = clientInfo.getRedirectUris();
        if (allowedUris == null || allowedUris.isEmpty()) {
            log.warn("客户端没有配置允许的回调地址: {}", clientId);
            throw new AppException("客户端未注册");
        }

        // 检查回调地址是否在白名单中（支持前缀匹配）
        boolean isAllowed = allowedUris.stream()
                .anyMatch(redirectUri::startsWith);

        if (!isAllowed) {
            log.warn("回调地址不在白名单中，clientId: {}, redirectUri: {}", clientId, redirectUri);
            throw new AppException("回调地址未授权");
        }
    }

    /**
     * 授权码信息内部类（用于Redis存储）
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    private static class AuthCodeInfo {
        /**
         * 授权码
         */
        private String code;

        /**
         * 客户端ID
         */
        private String clientId;

        /**
         * 用户登录token
         */
        private String loginToken;

        /**
         * 回调地址
         */
        private String redirectUri;

        /**
         * 状态参数
         */
        private String state;

        /**
         * 是否已使用
         */
        private Boolean used;

        /**
         * 过期时间
         */
        private LocalDateTime expireTime;
    }
}
