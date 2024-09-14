package com.lww.response;

/**
 * 返回结果响应码
 *
 * @author lww
 */
public enum ResponseCode {
    // 成功
    SUCCESS(200),

    // 失败
    FAILURE(400),

    // 未认证（签名错误）
    UNAUTHORIZED(401),

    // 访问被禁止
    FORBIDDEN(403),

    // 接口不存在
    NOT_FOUND(404),

    // 请求超时
    REQUEST_TIMEOUT(408),

    // 服务器内部错误
    INTERNAL_SERVER_ERROR(500),

    // 错误的网关
    BAD_GATEWAY(502),

    // 网关超时
    GATEWAY_TIMEOUT(504);

    public int code;

    ResponseCode(int code) {
        this.code = code;
    }
}
