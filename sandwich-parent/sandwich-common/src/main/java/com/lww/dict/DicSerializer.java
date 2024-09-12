package com.lww.dict;

/**
 *  基于JSON序列化  实现 字典转码 转码注解;
 *  该注解只有添加于String类型的属性上、且码表转换后的值不为null时生效
 * @author lww
 * @since 2024-09-12
 */

import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author zr 2024/2/28
 */

public class DicSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private String code;
    private String fieldName;


    public DicSerializer(String code, String fieldName) {
        this.code = code;
        this.fieldName = fieldName;
    }

    public DicSerializer() {
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(s);
        jsonGenerator.writeFieldName(fieldName);
        //这一步是通过code(字典类型code)和value(字段转换前的值)获取name(字段转换后的值)，
        String name = DicDataStore.getNameByCodeAndValue(this.code,s);
        jsonGenerator.writeString(name);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if(beanProperty !=null){
            if(Objects.equals (beanProperty.getType ().getRawClass (), String.class)){
                Dic t =beanProperty.getAnnotation (Dic.class);
                if(t !=null){
                    String beanFieldName = beanProperty.getName();
                    if(StringUtils.hasText(t.fieldName())){
                        beanFieldName = t.fieldName();
                    }
                    return new DicSerializer(t.code(),beanFieldName+"Text");
                }
            }
            return serializerProvider.findValueSerializer (beanProperty.getType (), beanProperty);
        }
        return serializerProvider.findNullValueSerializer (beanProperty);
    }
}
