package com.lww.oss.config;


import com.lww.oss.adapter.AliStorageAdapter;
import com.lww.oss.adapter.MinioStorageAdapter;
import com.lww.oss.adapter.StorageAdapter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储config     来决定当前使用哪种存储方式
 *
 * @author lww
 * @since 2023/10/14
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "oss.service")
public class StorageConfig {

    private String type;

    /**
     *  适配器模式和工厂+策略模式对比：
     *  适配器模式： 可能是两种模式来兼容使用，他们不一定是完全相似的，例如阿里云oss和minio  他们是两个厂家的。
     *  工厂+策略模式： 参考之前的题目分类，他们都是由一种类型衍生出来的，从而增加不同的类型功能，所以才采用工厂+策略模式。
     * <p>
     *  RefreshScope 注解：能够实现bean的动态加载  当storageType的值改变后 这个注解就会监听到 从而去重新在spring加载这个bean
     * <p>
     *
     *  适配器模式 切换阿里云oss/minio
     *  正常情况 有两种存储方式阿里云oss/minio  这里的设计是都实现了StorageAdapter这个接口，
     *  那么它有两个实现类，不指定的话 spring肯定是不知道用哪个实现的  就通过StorageAdapter做一个适配和FileService作为中转切换
     *
     */
    @Bean
    public StorageAdapter storageService() {
        if ("minio".equals(type)) {
            return new MinioStorageAdapter();
        } else if ("aliyun".equals(type)) {
            return new AliStorageAdapter();
        } else {
            throw new IllegalArgumentException("未找到对应的文件存储处理器");
        }
    }

}

