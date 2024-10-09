package com.lww.redis.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *  
 * @author lww
 * @since 2023/7/17 10:06
 */
@Slf4j
public class RedisUtil {

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private RedisTemplate<String,Object> redisTemplate;

        /**
         * 设置过期时间
         *
         * @param key  键
         * @param expireTime
         * @return
         */
        public  boolean expire(String key, long expireTime) {
            try {
                if (expireTime > 0) {
                    redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                }
                return true;
            } catch (Exception e) {
                log.error("设置过期时间error:", e);
                return false;
            }
        }

        /**
         *  获取key过期时间
         *
         * @param key
         * @return 时间(秒)
         */
        public  Long getExpire(String key) {
            if (!StringUtils.hasText(key)){
                return null;
            }
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        }

        /**
         * 判断key是否存在
         *
         * @param key 键
         * @return true 存在 false 不存在
         */
        public  Boolean hasKey(String key) {
            try {
                return redisTemplate.hasKey(key);
            } catch (Exception e) {
                log.error("hasKey判断error：", e);
                return false;
            }
        }

        /**
         * 删除缓存
         *
         * @param key 可以传一个或多个
         */
        public  void del(String... key) {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(Arrays.asList(key));
                }
            }
        }


        /**
         * 普通缓存获取
         *
         * @param key 键
         * @return 值
         */
        public  <T> T get(String key) {
            return key == null ? null : (T) redisTemplate.opsForValue().get(key);
        }

        /**
         * 普通缓存放入
         *
         * @param key   键
         * @param value 值
         * @return true成功 false失败
         */
        public  boolean set(String key, Object value) {
            try {
                if (Boolean.FALSE.equals(hasKey(key))){
                    redisTemplate.opsForValue().set(key, value);
                }
                return true;
            } catch (Exception e) {
                log.error("set redis key error：", e);
                return false;
            }

        }

        /**
         * 普通缓存放入并设置时间
         *
         * @param key   键
         * @param value 值
         * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
         * @return true成功 false 失败
         */
        public  boolean set(String key, Object value, long time) {
            try {
                if (time > 0) {
                    redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                } else {
                    set(key, value);
                }
                return true;
            } catch (Exception e) {
                log.error("set redis key error：", e);
                return false;
            }
        }
}
