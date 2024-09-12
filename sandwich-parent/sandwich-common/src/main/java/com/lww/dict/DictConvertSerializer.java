package com.lww.dict;

/**
 * 基于JSON序列化  实现 字典转码 转码注解;
 * 该注解只有添加于String类型的属性上、且码表转换后的值不为null时生效
 *
 * @author lww
 * @since 2024-09-12
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author zr 2024/2/28
 */

public class DictConvertSerializer extends JsonSerializer<String> {

    private String code;
    private String fieldName;


    public DictConvertSerializer(String code, String fieldName) {
        this.code = code;
        this.fieldName = fieldName;
    }

    public DictConvertSerializer() {
    }

    /** 
     * 序列化时 做操作
     * @return
     * @author lww
     * @since 
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        int a = 1/0;
        System.out.println(s);
        System.out.println(fieldName);
        System.out.println("----");
        // jsonGenerator.writeString(s);
        // jsonGenerator.writeFieldName(fieldName);
        // //这一步是通过code(字典类型code)和value(字段转换前的值)获取name(字段转换后的值)，
        // String name = DicDataStore.getNameByCodeAndValue(this.code, s);
        // jsonGenerator.writeString(name);
        jsonGenerator.writeString(s);
    }

}
