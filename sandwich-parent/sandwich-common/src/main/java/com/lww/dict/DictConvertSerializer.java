package com.lww.dict;

/**
 * 自定义序列化器类  实现 字典转码 转码注解;
 * 该注解只有添加于String类型的属性上、且码表转换后的值不为null时生效
 *
 * @author lww
 * @since 2024-09-12
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.lww.constant.PunctuationConstants;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lww 2024-09-13
 * createContextual 可以获得字段的类型以及注解。
 * createContextual 方法只会在第一次序列化字段时调用（因为字段的上下文信息在运行期不会改变），所以不用担心影响性能。
 */

public class DictConvertSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private String code;


    public DictConvertSerializer(String code) {
        this.code = code;
    }

    public DictConvertSerializer() {
    }

    /**
     * 序列化时 做操作
     * 告诉Jackson如何将Java对象转换为JSON
     *
     * @return
     * @author lww
     * @since
     */
    @Override
    public void serialize(String field, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 被打了dictConvert注解的字段 序列化时会进入到这里
        if (!StringUtils.hasLength(field)){
            jsonGenerator.writeString(field);
            return;
        }
        // 处理 传了多个字典值 逗号分割的情况
        String[] split = field.split(PunctuationConstants.COMMA);
        List<String> list = new ArrayList(split.length);
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            list.add(DicDataStore.getNameByCodeAndValue(this.code, s));
        }
        // 序列化的值要要写入json中
        jsonGenerator.writeObject(String.join(",", list));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 获取打的注解内容
                DictConvert dictConvert = beanProperty.getAnnotation(DictConvert.class);
                if (dictConvert == null) {
                    dictConvert = beanProperty.getContextAnnotation(DictConvert.class);
                }
                if (dictConvert != null) {
                    // 将注解的值设置进 自定义系列化器中
                    return new DictConvertSerializer(dictConvert.code());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
