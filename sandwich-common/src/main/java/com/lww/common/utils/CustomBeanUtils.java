package com.lww.common.utils;

/**
 * todo
 *
 * @author lww
 * @since 2025/12/11
 */

import java.lang.reflect.Constructor;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * 自定义 BeanUtils 工具类。
 * 提供对象属性复制功能，简化对象转换操作。
 * @author lww
 */
@Slf4j
@UtilityClass
public class CustomBeanUtils {

    /**
     * 将源对象 s 的属性复制到目标对象 t 中，并返回目标对象 t 的实例。
     *
     * @param s          源对象，包含要复制的属性
     * @param tClass     目标对象的 Class 类型
     * @param <S>        源对象的类型参数
     * @param <T>        目标对象的类型参数
     * @return           复制属性后的目标对象实例
     * @throws RuntimeException 如果目标对象的实例化失败或复制过程出错
     */
    public static <S, T> T copyProperties(S s, Class<T> tClass) {
        if (s == null) {
            return null;
        }
        // 检查参数是否为空，空参数会导致后续操作失败
        Assert.notNull(tClass, "Target class must not be null");
        try {
            // 通过反射创建目标对象的实例
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            T t = constructor.newInstance();
            // 复制源对象属性到目标对象
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (Exception e) {
            log.error("Failed to create instance of target class: {}", tClass.getName(), e);
            return null;
        }
    }

}

