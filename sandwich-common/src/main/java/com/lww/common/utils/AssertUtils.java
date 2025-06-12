package com.lww.common.utils;

import com.lww.common.web.exception.AppException;

/**
 * 断言工具类
 * @author sandw
 */
public class AssertUtils {

    private AssertUtils() {
    }

    public static void assertTrue(boolean condition, String errorMessage) {
        if (condition) {
            throw new AppException(errorMessage);
        }
    }

}
