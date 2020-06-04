package vip.mystery0.base.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.mystery0.base.springboot.config.BaseProperties;
import vip.mystery0.base.springboot.service.redis.IRedis;
import vip.mystery0.tools.java.factory.JsonFactory;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * @author mystery0
 * Create at 2020/5/11
 */
@SuppressWarnings("unchecked")
@Service
public class JRedisService {
    private static final Logger log = LoggerFactory.getLogger(JRedisService.class);

    @Autowired
    private BaseProperties properties;
    @Autowired
    private IRedis iRedis;

    public String getRedisKey(String key) {
        return properties.getRedisPrefix() + ":" + key;
    }

    /**
     * 将数据放到redis中
     *
     * @param key   键
     * @param value 值
     */
    public void putIntoRedis(String key,
                             Object value) {
        putIntoRedis(key, value, -1L);
    }

    /**
     * 将数据放到redis中
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间
     */
    public void putIntoRedis(String key,
                             Object value,
                             long expireTime) {
        String redisKey = getRedisKey(key);
        if (value == null) {
            log.warn("put into redis but value is null, key: {}", redisKey);
            return;
        }
        String redisValue = JsonFactory.toJson(value);
        log.debug("put into redis, key: {}, value: {}, expireTime: {}ms", redisKey, redisValue, expireTime);
        if (expireTime == -1L) {
            iRedis.set(redisKey, redisValue);
        } else {
            iRedis.set(redisKey, redisValue, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 从redis中获取值
     *
     * @param key    键
     * @param tClass 返回数据类型
     * @param <T>    数据类型
     * @return 原始数据
     */
    public <T> T getFromRedis(String key,
                              Class<T> tClass) {
        try {
            String redisKey = getRedisKey(key);
            log.debug("get key value from redis, key: {}", redisKey);
            String redisValue = iRedis.get(redisKey);
            if (redisValue == null) {
                return null;
            }
            log.debug("get value: {}", redisValue);
            if (tClass == String.class) {
                return (T) redisValue;
            }
            return JsonFactory.fromJson(redisValue, tClass);
        } catch (Exception e) {
            log.error("get key value from redis error", e);
            return null;
        }
    }

    /**
     * 从redis中获取值
     *
     * @param key  键
     * @param type 返回数据类型
     * @param <T>  数据类型
     * @return 原始数据
     */
    public <T> T getFromRedis(String key,
                              Type type) {
        try {
            String redisKey = getRedisKey(key);
            log.debug("get key value from redis, key: {}", redisKey);
            String redisValue = iRedis.get(redisKey);
            if (redisValue == null) {
                return null;
            }
            log.debug("get value: {}", redisValue);
            if (type == String.class) {
                return (T) redisValue;
            }
            return JsonFactory.fromJson(redisValue, type);
        } catch (Exception e) {
            log.error("get key value from redis error", e);
            return null;
        }
    }

    /**
     * 设置redis键过期时间
     *
     * @param key        键名
     * @param expireTime 过期时间：单位毫秒
     */
    public void updateRedisKeyExpire(String key, long expireTime) {
        String redisKey = getRedisKey(key);
        log.debug("update redis key expire time, key: {}, expire time: {}", redisKey, expireTime);
        try {
            iRedis.expire(redisKey, expireTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("update redis key expire time error, key: {}, expire time: {}ms", redisKey, expireTime, e);
        }
    }

    /**
     * 删除指定的key
     *
     * @param key 键名
     */
    public void deleteRedisKey(String key) {
        String redisKey = getRedisKey(key);
        log.debug("delete redis key, key: {}", redisKey);
        try {
            iRedis.del(redisKey);
        } catch (Exception e) {
            log.error("delete redis key error", e);
        }
    }
}
