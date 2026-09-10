package com.lww.core.dict;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 基于 Redis Hash 的共享字典存储。
 *
 * @author lww
 */
public class RedisDictStore implements DictStore {

    private static final String CACHE_KEY_PREFIX = "sandwich:dict:";

    private final StringRedisTemplate redisTemplate;

    public RedisDictStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String resolve(String typeCode, String value) {
        if (!StringUtils.hasText(typeCode) || value == null) {
            return value;
        }
        Object label = redisTemplate.opsForHash().get(cacheKey(typeCode), value);
        return label == null ? value : label.toString();
    }

    @Override
    public void replace(String typeCode, Map<String, String> items) {
        if (!StringUtils.hasText(typeCode)) {
            return;
        }
        String key = cacheKey(typeCode);
        redisTemplate.delete(key);
        if (items != null && !items.isEmpty()) {
            redisTemplate.opsForHash().putAll(key, items);
        }
    }

    @Override
    public void remove(String typeCode) {
        if (StringUtils.hasText(typeCode)) {
            redisTemplate.delete(cacheKey(typeCode));
        }
    }

    private String cacheKey(String typeCode) {
        return CACHE_KEY_PREFIX + typeCode;
    }
}
