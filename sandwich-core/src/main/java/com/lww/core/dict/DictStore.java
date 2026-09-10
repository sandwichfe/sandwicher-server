package com.lww.core.dict;

import java.util.Map;

/**
 * 字典存储接口，隔离字段翻译与具体缓存实现。
 *
 * @author lww
 */
public interface DictStore {

    /**
     * 获取字典值对应的展示文本。
     *
     * @param typeCode 字典类型编码
     * @param value    字典值
     * @return 展示文本；不存在时返回原字典值
     */
    String resolve(String typeCode, String value);

    /**
     * 使用最新数据整体替换一个字典类型。
     *
     * @param typeCode 字典类型编码
     * @param items    字典值与展示文本
     */
    void replace(String typeCode, Map<String, String> items);

    /**
     * 删除一个字典类型的缓存。
     *
     * @param typeCode 字典类型编码
     */
    void remove(String typeCode);
}
