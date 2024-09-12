package com.lww.constant;

/**
 * @author lww
 * @since 2024-09-12
 */
/**
 * HTTP状态码常量类。
 * 包含常见的HTTP响应状态码及其中文描述。
 */
public final class HttpStatusCodes {

    /**
     * 请求成功
     */
    public static final int OK = 200;

    /**
     * 请求成功并创建了新的资源
     */
    public static final int CREATED = 201;

    /**
     * 请求已接受但尚未处理
     */
    public static final int ACCEPTED = 202;

    /**
     * 请求成功但返回的信息可能来自缓存
     */
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;

    /**
     * 请求成功但无返回内容
     */
    public static final int NO_CONTENT = 204;

    /**
     * 请求成功且要求客户端重置视图
     */
    public static final int RESET_CONTENT = 205;

    /**
     * 部分内容已被成功请求
     */
    public static final int PARTIAL_CONTENT = 206;

    /**
     * 多种选择
     */
    public static final int MULTIPLE_CHOICES = 300;

    /**
     * 资源已被永久移动
     */
    public static final int MOVED_PERMANENTLY = 301;

    /**
     * 资源临时移动
     */
    public static final int FOUND = 302;

    /**
     * 资源在不同的URI中
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源未被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 资源必须通过代理访问
     */
    public static final int USE_PROXY = 305;

    /**
     * 临时重定向
     */
    public static final int TEMPORARY_REDIRECT = 307;

    /**
     * 永久重定向
     */
    public static final int PERMANENT_REDIRECT = 308;

    /**
     * 错误的请求
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 需要身份验证
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 需要付款（保留状态码）
     */
    public static final int PAYMENT_REQUIRED = 402;

    /**
     * 访问被禁止
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 方法不被允许
     */
    public static final int METHOD_NOT_ALLOWED = 405;

    /**
     * 无法接受的请求
     */
    public static final int NOT_ACCEPTABLE = 406;

    /**
     * 代理身份验证失败
     */
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;

    /**
     * 请求超时
     */
    public static final int REQUEST_TIMEOUT = 408;

    /**
     * 请求冲突
     */
    public static final int CONFLICT = 409;

    /**
     * 资源已被永久删除
     */
    public static final int GONE = 410;

    /**
     * 需要Content-Length头
     */
    public static final int LENGTH_REQUIRED = 411;

    /**
     * 预条件失败
     */
    public static final int PRECONDITION_FAILED = 412;

    /**
     * 请求负载过大
     */
    public static final int PAYLOAD_TOO_LARGE = 413;

    /**
     * 请求URI过长
     */
    public static final int URI_TOO_LONG = 414;

    /**
     * 不支持的媒体类型
     */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * 请求范围不满足
     */
    public static final int RANGE_NOT_SATISFIABLE = 416;

    /**
     * 期望失败
     */
    public static final int EXPECTATION_FAILED = 417;

    /**
     * 我是一个茶壶（愚弄的状态码）
     */
    public static final int IM_A_TEAPOT = 418;

    /**
     * 服务器内部错误
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * 未实现
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * 错误的网关
     */
    public static final int BAD_GATEWAY = 502;

    /**
     * 服务不可用
     */
    public static final int SERVICE_UNAVAILABLE = 503;

    /**
     * 网关超时
     */
    public static final int GATEWAY_TIMEOUT = 504;

    /**
     * HTTP版本不受支持
     */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * 变体也进行协商
     */
    public static final int VARIANT_ALSO_NEGOTIATES = 506;

    /**
     * 存储不足
     */
    public static final int INSUFFICIENT_STORAGE = 507;

    /**
     * 检测到循环
     */
    public static final int LOOP_DETECTED = 508;

    /**
     * 未扩展
     */
    public static final int NOT_EXTENDED = 510;

    private HttpStatusCodes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

