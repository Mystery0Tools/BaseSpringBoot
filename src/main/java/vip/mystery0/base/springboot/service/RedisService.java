package vip.mystery0.base.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vip.mystery0.base.springboot.config.PropertiesConfig;
import vip.mystery0.tools.java.factory.JsonFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Component
public class RedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private PropertiesConfig propertiesConfig;
    @Resource
    private StringRedisTemplate redisTemplate;

    public String getRedisKey(String key) {
        return propertiesConfig.getRedisPrefix() + ":" + key;
    }

    /**
     * 将数据放到redis中
     *
     * @param key   键
     * @param value 值
     */
    public void putIntoRedis(String key, Object value) {
        try {
            String redisKey = getRedisKey(key);
            String redisValue = JsonFactory.toJson(value);
            log.debug("put into redis, key: {}, value: {}", redisKey, redisValue);
            redisTemplate.opsForValue().set(redisKey, redisValue);
        } catch (Exception e) {
            log.error("put into redis failed", e);
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
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            log.debug("get value: {}", redisValue);
            return JsonFactory.fromJson(redisValue, tClass);
        } catch (Exception e) {
            log.error("get key value from redis error", e);
        }
        return null;
    }

    /**
     * 设置redis键过期时间
     *
     * @param key        键名
     * @param expireTime 过期时间：单位毫秒
     */
    public void updateRedisKeyExpire(String key,
                                     long expireTime) {
        String redisKey = getRedisKey(key);
        log.debug("update redis key expire time, key: {}, expire time: {}", redisKey, expireTime);
        try {
            redisTemplate.expire(redisKey, expireTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("update redis key expire time error, key: {}, expire time: {}", redisKey, expireTime);
        }
    }
}
