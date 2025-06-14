package com.lww.auth.server.user.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.lww.auth.server.user.service.QrCodeLoginService;
import com.lww.auth.server.user.vo.QrCodeInfo;
import com.lww.auth.server.user.vo.req.QrCodeLoginConsentRequest;
import com.lww.auth.server.user.vo.req.QrCodeLoginScanResponse;
import com.lww.auth.server.user.vo.resp.QrCodeLoginFetchResponse;
import com.lww.common.web.exception.AppException;
import com.lww.common.web.vo.User;
import com.lww.redis.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 二维码登录接口实现
 *
 * @author lww
 * @since 2024/12/13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QrCodeLoginServiceImpl implements QrCodeLoginService {

    /**
     * ?wei
     */
    private final RedisUtil resultUtil;

    /**
     * 过期时间
     */
    private static final long QR_CODE_INFO_TIMEOUT = (60 * 10);

    /**
     * 二维码信息前缀
     */
    private static final String QR_CODE_PREV = "login:qrcode:";

    @Override
    public String generateQrCode() {
        // 生成二维码唯一id
        String qrCodeId = IdWorker.getIdStr();
        QrCodeInfo info = QrCodeInfo.builder()
                .qrCodeId(qrCodeId)
                // 待扫描状态
                .qrCodeStatus(0)
                // 1分钟后过期
                .expiresTime(LocalDateTime.now().plusMinutes(2L))
                .build();

        // 因为上边设置的过期时间是2分钟，这里设置10分钟过期，可根据业务自行调整过期时间
        resultUtil.set(QR_CODE_PREV + qrCodeId, info, QR_CODE_INFO_TIMEOUT);
        return qrCodeId;
    }

    @Override
    public QrCodeLoginFetchResponse fetch(String qrCodeId) {

        // 校验二维码状态
        QrCodeInfo info = resultUtil.get(QR_CODE_PREV + qrCodeId);
        if (info == null) {
            throw new AppException("无效二维码或二维码已过期.");
        }

        QrCodeLoginFetchResponse loginFetchResponse = new QrCodeLoginFetchResponse();
        // 设置二维码是否过期、状态
        loginFetchResponse.setQrCodeStatus(info.getQrCodeStatus());
        loginFetchResponse.setExpired(info.getExpiresTime().isBefore(LocalDateTime.now()));

        // 如果是还 未扫描 或者扫描待确认 直接返回当前状态
        if (info.getQrCodeStatus() == 0 || info.getQrCodeStatus() == 1) {
            loginFetchResponse.setName(info.getName());
            loginFetchResponse.setAvatarUrl(info.getAvatarUrl());
            loginFetchResponse.setQrCodeStatus(info.getQrCodeStatus());
            return loginFetchResponse;
        }

        // 如果是已确认
        if (!Objects.equals(info.getQrCodeStatus(), 2)) {
            throw new AppException("二维码已失效");
        }

        // 根据二维码id从redis获取用户信息
        String redisUserinfoKey = String.format("%s%s:%s", QR_CODE_PREV, "userinfo", qrCodeId);
        String token = resultUtil.get(redisUserinfoKey);
        if (StringUtils.isBlank(token)) {
            throw new AppException("获取登录确认用户信息失败.");
        }

        // 拿到用户信息 之后 就要想办法获取token 返回给前端就好了
        // 操作成功后移除缓存
        resultUtil.del(QR_CODE_PREV + qrCodeId);
        // 删除用户信息，防止其它人重放请求
        resultUtil.del(redisUserinfoKey);

        // 返回token
        loginFetchResponse.setToken(token);
        return loginFetchResponse;
    }



    @Override
    public QrCodeLoginScanResponse scan(String qrCodeId) {
        // 应该用validation的
        Assert.hasLength(qrCodeId, "二维码Id不能为空.");

        // 校验二维码状态
        QrCodeInfo info = resultUtil.get(QR_CODE_PREV + qrCodeId);
        if (info == null) {
            throw new AppException("无效二维码.");
        }

        // 验证状态
        if (!Objects.equals(info.getQrCodeStatus(), 0)) {
            throw new AppException("二维码已被其他人扫描，无法重复扫描.");
        }

        // 二维码是否过期
        boolean qrCodeExpire = info.getExpiresTime().isBefore(LocalDateTime.now());
        if (qrCodeExpire) {
            throw new AppException("二维码已过期.");
        }

        // 获取当前登录用户信息  这里是手机端请求这个接口  也就是肯定是已经在手机上登录了的用户
        User user = new User();
        // 生成临时票据 用于确认接口
        String qrCodeTicket = IdWorker.getIdStr();
        // 根据二维码id和临时票据存储，确认时根据临时票据认证
        String redisQrCodeTicketKey = String.format("%s%s:%s", QR_CODE_PREV, qrCodeId, qrCodeTicket);
        resultUtil.set(redisQrCodeTicketKey, qrCodeTicket, QR_CODE_INFO_TIMEOUT);

        // 更新二维码信息的状态
        info.setQrCodeStatus(1);
        // 用户名
        // info.setName(basicUser.getName());
        // 用户头像
        // info.setAvatarUrl(basicUser.getAvatarUrl());
        resultUtil.set(QR_CODE_PREV + qrCodeId, info, QR_CODE_INFO_TIMEOUT);

        // 返回扫码相关信息
        return new QrCodeLoginScanResponse()
                .setQrCodeTicket(qrCodeTicket)
                .setQrCodeStatus(1)
                .setExpired(qrCodeExpire)
                .setScopes(info.getScopes());
    }

    @Override
    public void consent(QrCodeLoginConsentRequest loginConsent, HttpServletRequest request) {
        // 应该用validation的
        Assert.hasLength(loginConsent.getQrCodeId(), "二维码Id不能为空.");

        // 校验二维码状态
        QrCodeInfo info = resultUtil.get(QR_CODE_PREV + loginConsent.getQrCodeId());
        if (info == null) {
            throw new AppException("无效二维码或二维码已过期.");
        }

        // 验证临时票据
        String qrCodeTicketKey = String.format("%s%s:%s", QR_CODE_PREV, loginConsent.getQrCodeId(), loginConsent.getQrCodeTicket());
        String redisQrCodeTicket = resultUtil.get(qrCodeTicketKey);
        if (!Objects.equals(redisQrCodeTicket, loginConsent.getQrCodeTicket())) {
            // 临时票据有误、临时票据失效(超过redis存活时间后确认)、redis数据有误
            if (log.isDebugEnabled()) {
                log.debug("临时票据有误、临时票据失效(超过redis存活时间后确认)、redis数据有误.");
            }
            throw new AppException("登录确认失败，请重新扫描.");
        }
        // 使用后删除
        resultUtil.del(qrCodeTicketKey);

        // 获取登录用户信息

        // 根据二维码id 放入此用户 token 信息  用于fetch接口返回 用户登录信息
        // 我这里是 直接 把当前用户的token丢出去了 其他方式应该都可以 直接能代表是这个用户登录就行
        String redisUserinfoKey = String.format("%s%s:%s", QR_CODE_PREV, "userinfo", loginConsent.getQrCodeId());
        // 存储用户信息
        resultUtil.set(redisUserinfoKey, request.getHeader("Authorization"), QR_CODE_INFO_TIMEOUT);

        // 更新二维码信息的状态
        info.setQrCodeStatus(2);
        resultUtil.set(QR_CODE_PREV + loginConsent.getQrCodeId(), info, QR_CODE_INFO_TIMEOUT);
    }


}
