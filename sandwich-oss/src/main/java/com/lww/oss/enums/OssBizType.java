package com.lww.oss.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 上传业务类型。
 * <p>
 * 目录由服务端按业务类型决定，避免前端随意指定路径、把桶名或其他业务目录带进 key。
 * 新增业务时在这里补充枚举即可。
 *
 * @author lww
 * @since 2026-10-26
 */
public enum OssBizType {

    /**
     * 笔记正文图片
     */
    NOTE_IMAGE("note-image", "note/image"),

    /**
     * 用户头像
     */
    AVATAR("avatar", "avatar"),

    /**
     * 附件
     */
    ATTACHMENT("attachment", "attachment"),

    /**
     * 合同等业务文件
     */
    CONTRACT("contract", "contract");

    private final String code;

    private final String dir;

    OssBizType(String code, String dir) {
        this.code = code;
        this.dir = dir;
    }

    public String getCode() {
        return code;
    }

    public String getDir() {
        return dir;
    }

    /**
     * 按编码解析业务类型，未匹配时返回 null，由调用方提示合法取值
     */
    public static OssBizType of(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(type -> type.code.equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 全部合法编码，用于错误提示
     */
    public static String codes() {
        return Arrays.stream(values())
                .map(OssBizType::getCode)
                .collect(Collectors.joining("、"));
    }
}
