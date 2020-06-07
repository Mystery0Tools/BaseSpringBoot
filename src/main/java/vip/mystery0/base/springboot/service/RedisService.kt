package vip.mystery0.base.springboot.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.service.redis.IRedis
import vip.mystery0.base.springboot.utils.withError
import vip.mystery0.tools.kotlin.factory.JsonFactory
import vip.mystery0.tools.kotlin.factory.fromJson
import vip.mystery0.tools.kotlin.factory.toJson
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * @author mystery0
 */
@Suppress("unchecked_cast")
@Service
class RedisService(
    objectMapper: ObjectMapper,
    private val properties: BaseProperties,
    private val iRedis: IRedis
) {
    companion object {
        private val log = LoggerFactory.getLogger(RedisService::class.java)
    }

    init {
        JsonFactory.setObjectMapper(objectMapper)
    }

    fun getRedisKey(key: String): String = properties.redisPrefix + ":" + key

    /**
     * 将数据放到redis中
     *
     * @param key   键
     * @param value 值
     */
    fun putIntoRedis(key: String, value: Any?, expireTime: Long = -1L) {
        log.withError("put into redis failed") {
            val redisKey = getRedisKey(key)
            if (value == null) {
                log.warn("put into redis but value is null, key: {}", redisKey)
                return@withError
            }
            val redisValue = value.toJson()
            log.debug("put into redis, key: {}, value: {}, expireTime: {}ms", redisKey, redisValue, expireTime)
            if (expireTime == -1L)
                iRedis.set(redisKey, redisValue)
            else
                iRedis.set(redisKey, redisValue, expireTime, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 从redis中获取值
     *
     * @param key    键
     * @param type   返回数据类型
     * @param <T>    数据类型
     * @return 原始数据
     */
    fun <T : Any> getFromRedis(key: String, clazz: Class<T>): T? = log.withError<T>("get key value from redis error") {
        val redisKey = getRedisKey(key)
        log.debug("get key value from redis, key: {}", redisKey)
        val redisValue = iRedis.get(redisKey) ?: return@withError null
        log.debug("get value: {}", redisValue)
        if (clazz == String::class.java) return@withError redisValue as T?
        return@withError redisValue.fromJson(clazz)
    }

    /**
     * 从redis中获取值
     *
     * @param key    键
     * @param type   返回数据类型
     * @param <T>    数据类型
     * @return 原始数据
     */
    fun <T : Any> getFromRedis(key: String, clazz: KClass<T>): T? = log.withError<T>("get key value from redis error") {
        val redisKey = getRedisKey(key)
        log.debug("get key value from redis, key: {}", redisKey)
        val redisValue = iRedis.get(redisKey) ?: return@withError null
        log.debug("get value: {}", redisValue)
        if (clazz == String::class) return@withError redisValue as T?
        return@withError redisValue.fromJson(clazz)
    }

    /**
     * 从redis中获取值
     *
     * @param key    键
     * @param type   返回数据类型
     * @param <T>    数据类型
     * @return 原始数据
     */
    fun <T> getFromRedis(key: String, type: Type): T? = log.withError<T>("get key value from redis error") {
        val redisKey = getRedisKey(key)
        log.debug("get key value from redis, key: {}", redisKey)
        val redisValue = iRedis.get(redisKey) ?: return@withError null
        log.debug("get value: {}", redisValue)
        if (type.typeName == String::class.java.typeName) return@withError redisValue as T?
        return@withError redisValue.fromJson(type)
    }

    /**
     * 设置redis键过期时间
     *
     * @param key        键名
     * @param expireTime 过期时间：单位毫秒
     */
    fun updateRedisKeyExpire(key: String, expireTime: Long) {
        val redisKey = getRedisKey(key)
        log.debug("update redis key expire time, key: {}, expire time: {}", redisKey, expireTime)
        log.withError("update redis key expire time error, key: $redisKey, expire time: ${expireTime}ms") {
            iRedis.expire(redisKey, expireTime, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 删除指定的key
     */
    fun deleteRedisKey(key: String) {
        val redisKey = getRedisKey(key)
        log.debug("delete redis key, key: {}", redisKey)
        log.withError("delete redis key error") {
            iRedis.del(redisKey)
        }
    }
}