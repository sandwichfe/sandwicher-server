package com.lww.core.dict;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 字典翻译自动配置。
 *
 * @author lww
 */
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DictAutoConfiguration {

    /**
     * 提供默认 Redis 字典存储，业务应用可以声明 {@link DictStore} 覆盖。
     *
     * @param redisTemplate 字符串 Redis 模板
     * @return 字典存储
     */
    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    @ConditionalOnMissingBean(DictStore.class)
    public DictStore dictStore(StringRedisTemplate redisTemplate) {
        return new RedisDictStore(redisTemplate);
    }
}
