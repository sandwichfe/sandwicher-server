package com.lww.dict;

/**
 * @author lww
 * @since 2024 09 12

 * @Description 嵌入式转译注解，生效点：request请求完成返回时，进行序列化过滤，并嵌入实现
 * @Description 失效点： service业务处理层是不会生效的，序列化对象转Json,或者其他格式对象后，也是无法过滤到的
 */

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import java.lang.annotation.*;

@JacksonAnnotationsInside
@JsonSerialize(
        using = DictConvertSerializer.class
)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictConvert {
    String code();

    String fieldName() default "";
}

