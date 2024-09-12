package com.lww.dict;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 转码相关处理组件  监听spring启动时 完成初始化缓存
 *
 * @author lww
 * @since 2024-9-12
 **/
@Component
@Slf4j
public class DicInitComponent implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("-----------------开始缓存字典数据-----------------");
        // 查数据库放进去 暂时todo

        String statusType = "statusType";
        Map<Object,String> m1 = new HashMap(2);
        m1.put("0","初始化");
        DicDataStore.putValue(statusType,m1);

        String sexType = "sexType";
        Map<Object,String> m2 = new HashMap(2);
        m2.put("1","男");
        m2.put("2","女");
        DicDataStore.putValue(sexType,m2);

        log.info("-----------------缓存字典数据完成-----------------");
    }

}

