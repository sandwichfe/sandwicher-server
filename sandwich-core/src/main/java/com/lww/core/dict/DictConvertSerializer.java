package com.lww.core.dict;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.lww.common.constant.PunctuationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Jackson 字典转码序列化器。
 * 被 {@link DictConvert} 标记的字段会保留原值，并额外输出对应的展示文本字段。
 *
 * @author lww
 */
public class DictConvertSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictConvertSerializer.class);

    private String code;

    private String dictName;

    @Autowired(required = false)
    private DictStore dictStore;

    public DictConvertSerializer() {
    }

    private DictConvertSerializer(String code, String dictName, DictStore dictStore) {
        this.code = code;
        this.dictName = dictName;
        this.dictStore = dictStore;
    }

    @Override
    public void serialize(String fieldValue, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(fieldValue);
        if (!StringUtils.hasLength(fieldValue)) {
            return;
        }

        String[] values = fieldValue.split(PunctuationConstants.COMMA);
        List<String> labels = new ArrayList<>(values.length);
        for (String value : values) {
            labels.add(resolveSafely(value));
        }
        jsonGenerator.writeStringField(dictName, String.join(PunctuationConstants.COMMA, labels));
    }

    private String resolveSafely(String value) {
        if (dictStore == null) {
            return value;
        }
        try {
            return dictStore.resolve(code, value);
        } catch (RuntimeException e) {
            // 字典缓存异常不能影响正常业务响应，翻译失败时保留原值。
            LOGGER.warn("字典翻译失败，typeCode={}, value={}", code, value, e);
            return value;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider,
                                               BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null || !Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            return serializerProvider.findNullValueSerializer(null);
        }

        DictConvert dictConvert = beanProperty.getAnnotation(DictConvert.class);
        if (dictConvert == null) {
            dictConvert = beanProperty.getContextAnnotation(DictConvert.class);
        }
        if (dictConvert == null) {
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }

        String fieldName = beanProperty.getName();
        String textFieldName = StringUtils.hasText(dictConvert.fieldName())
                ? dictConvert.fieldName()
                : fieldName + "Value";
        return new DictConvertSerializer(dictConvert.code(), textFieldName, dictStore);
    }
}
