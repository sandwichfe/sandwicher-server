package com.lww.common.dict;

/**
 * 字典数据
 *
 * @author lww
 * @since 1.4.0
 */

import java.util.HashMap;
import java.util.Map;

public class DicDataStore {

    public static final Map<String, Map<Object, String>> dicData = new HashMap<>();

    static {
        // 1.可在spring启动时 放入
        // 2.或者本地缓存 以及 redis缓存  二级缓存模式(caffine+redis)     在此处就暂时采用本地缓存好了.
        // Map<Object,String> m1 = new HashMap();
        // m1.put("0","初始化");
        // dicData.put("carStatus",m1);
    }

    public static void putValue(String type, Map<Object, String> map) {
        dicData.put(type, map);
    }


    public static String getNameByCodeAndValue(String code, String value) {
        return dicData.get(code).get(value);
    }

}

