package com.lww.common.web.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lww
 */
@Target(ElementType.METHOD)  // 作用于方法
@Retention(RetentionPolicy.RUNTIME)  // 运行时生效
@Documented
public @interface Loggable {
    /** 操作模块 */
    String module() default "";
    
    /** 操作类型 */
    String type() default "";
    
    /** 操作描述（支持SpEL表达式） */
    String description() default "";
    
    /** 是否记录方法参数 */
    boolean logParams() default true;
    
    /** 是否记录返回值 */
    boolean logResult() default false;
    
    /** 是否异步记录日志 */
    boolean async() default true;
    
    /** 是否记录异常堆栈 */
    boolean logStackTrace() default false;
    
    /** 敏感参数过滤（支持SpEL表达式） */
    String[] sensitiveParams() default {};
    
    /** 超时警告阈值（毫秒） */
    long timeoutThreshold() default 0L;
}