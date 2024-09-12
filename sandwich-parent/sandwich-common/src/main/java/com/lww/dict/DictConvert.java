package com.lww.dict;

/**
 * @author lww
 * @since 2024 09 12
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
