package com.lww.constant;

/**
 * 工具类，定义了常见的标点符号常量。
 * 该类不应被实例化。
 * @author lww
 */
public final class PunctuationConstants {

    /**
     * 私有构造函数，防止实例化
     *
     * @author lww
     * @since 2024-9-12
     */
    private PunctuationConstants() {
        throw new AssertionError("无法实例化 PunctuationConstants 类");
    }

    /** 逗号标点符号。 */
    public static final String COMMA = ",";

    /** 句号标点符号。 */
    public static final String PERIOD = ".";

    /** 感叹号标点符号。 */
    public static final String EXCLAMATION_MARK = "!";

    /** 问号标点符号。 */
    public static final String QUESTION_MARK = "?";

    /** 冒号标点符号。 */
    public static final String COLON = ":";

    /** 分号标点符号。 */
    public static final String SEMICOLON = ";";

    /** 双引号标点符号。 */
    public static final String QUOTE = "\"";

    /** 单引号标点符号。 */
    public static final String APOSTROPHE = "'";

    /** 连字符标点符号（常用于范围或连接词）。 */
    public static final String DASH = "-";

    /** 短横线标点符号（常用于连接词）。 */
    public static final String HYPHEN = "-";

    /** 斜杠标点符号。 */
    public static final String SLASH = "/";

    /** 反斜杠标点符号。 */
    public static final String BACKSLASH = "\\";

    /** 左圆括号标点符号。 */
    public static final String OPEN_PARENTHESIS = "(";

    /** 右圆括号标点符号。 */
    public static final String CLOSE_PARENTHESIS = ")";

    /** 左方括号标点符号。 */
    public static final String OPEN_BRACKET = "[";

    /** 右方括号标点符号。 */
    public static final String CLOSE_BRACKET = "]";

    /** 左大括号标点符号。 */
    public static final String OPEN_BRACE = "{";

    /** 右大括号标点符号。 */
    public static final String CLOSE_BRACE = "}";

    /** 省略号标点符号，用于表示省略的文本。 */
    public static final String ELLIPSIS = "...";

}
