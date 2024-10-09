package com.lww.security.config;

/**
 * 常量类
 *
 * @author lww
 * @since 2022/7/21 10:01
 */
public class SecurityConstant {

    private SecurityConstant() {
    }

    /**
     * token分割
     */
    public static final String TOKEN_SPLIT = "Bearer ";

    /**
     * JWT签名加密key
     */
    public static final String JWT_SIGN_KEY = "sandwich";

    /**
     * token参数头
     */
    public static final String HEADER = "accessToken";

    /**
     * 权限参数头
     */
    public static final String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    public static final String SAVE_LOGIN = "saveLogin";

    /**
     * 交互token前缀key
     */
    public static final String TOKEN_PRE = "TTYY_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    public static final String USER_TOKEN = "TTYY_USER_TOKEN:";

}
