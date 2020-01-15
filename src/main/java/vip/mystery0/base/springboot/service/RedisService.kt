package vip.mystery0.base.springboot.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.tools.java.factory.JsonFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * @author mystery0
 */
@Service
class RedisService {
    companion object {
        private val log = LoggerFactory.getLogger(RedisService::class.java)
        //默认过期时间30分钟
        private const val defaultExpireTime = 30 * 60 * 1000L
    }

    @Autowired
    private lateinit var properties: BaseProperties
    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate

    fun getRedisKey(key: String): String = properties.redisPrefix + ":" + key

    /**
     * 将数据放到redis中
     *
     * @param key   键
     * @param value 值
     */
    fun putIntoRedis(key: String, value: Any?) = putIntoRedis(key, value, defaultExpireTime)

    /**
     * 将数据放到redis中
     *
     * @param key   键
     * @param value 值
     */
    fun putIntoRedis(key: String, value: Any?, expireTime: Long) {
        try {
            val redisKey = getRedisKey(key)
            if (value == null) {
                log.warn("put into redis but value is null, key: {}", redisKey)
                return
            }
            val redisValue = JsonFactory.toJson(value)
            log.info("put into redis, key: {}, value: {}, expireTime: {}ms", redisKey, redisValue, expireTime)
            redisTemplate.opsForValue().set(redisKey, redisValue, expireTime, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            log.error("put into redis failed", e)
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
    fun <T> getFromRedis(key: String, tClass: Class<T>): T? {
        try {
            val redisKey = getRedisKey(key)
            log.info("get key value from redis, key: {}", redisKey)
            val redisValue = redisTemplate.opsForValue()[redisKey] ?: return null
            log.info("get value: {}", redisValue)
            return JsonFactory.fromJson(redisValue, tClass)
        } catch (e: Exception) {
            log.error("get key value from redis error", e)
            return null
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
    fun <T> getFromRedis(key: String, type: Type): T? {
        try {
            val redisKey = getRedisKey(key)
            log.info("get key value from redis, key: {}", redisKey)
            val redisValue = redisTemplate.opsForValue()[redisKey] ?: return null
            log.info("get value: {}", redisValue)
            return JsonFactory.fromJson(redisValue, type)
        } catch (e: Exception) {
            log.error("get key value from redis error", e)
            return null
        }
    }

    /**
     * 设置redis键过期时间
     *
     * @param key        键名
     * @param expireTime 过期时间：单位毫秒
     */
    fun updateRedisKeyExpire(key: String, expireTime: Long) {
        val redisKey = getRedisKey(key)
        log.info("update redis key expire time, key: {}, expire time: {}", redisKey, expireTime)
        try {
            redisTemplate.expire(redisKey, expireTime, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            log.error("update redis key expire time error, key: {}, expire time: {}ms", redisKey, expireTime)
        }
    }
}