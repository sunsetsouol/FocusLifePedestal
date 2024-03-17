package com.qgStudio.pedestal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2023/11/16
 */
@Component
public class RedisCache {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 缓存基本对象
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    public void setCacheObject(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本对象
     *
     * @param key      缓存键
     * @param value    缓存值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public void setCacheObject(final String key, final String value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key      缓存键
     * @param timeout  超时时间
     * @param timeUnit 时间粒度
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键
     * @return 缓存键对应的数据
     */
    public String getCacheObject(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteObject(String redisNewersKey) {
        redisTemplate.delete(redisNewersKey);
    }

    public Integer getIncrement(String key) {
        return redisTemplate.opsForValue().increment(key).intValue();
    }

}
