package vip.mystery0.base.springboot.service.redis.impl

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import vip.mystery0.base.springboot.service.redis.IRedis
import java.util.concurrent.TimeUnit

/**
 * @author mystery0
 * Create at 2020/3/21
 */
@Repository("iRedis")
class IRedisImpl(private val redisTemplate: StringRedisTemplate) : IRedis {
    private val valueOperations = redisTemplate.opsForValue()
    private val listOperations = redisTemplate.opsForList()

    override fun ttl(key: String): Long = redisTemplate.getExpire(key)

    override fun expire(key: String, timeout: Long, timeUnit: TimeUnit): Boolean =
        redisTemplate.expire(key, timeout, timeUnit)

    override fun incr(key: String): Long? = valueOperations.increment(key)

    override fun incr(key: String, delta: Long): Long? = valueOperations.increment(key, delta)

    override fun keys(pattern: String): Set<String> = redisTemplate.keys(pattern)

    override fun del(key: String): Boolean = redisTemplate.delete(key)

    override fun set(key: String, value: String) = valueOperations.set(key, value)

    override fun set(key: String, value: String, timeout: Long, timeUnit: TimeUnit) =
        valueOperations.set(key, value, timeout, timeUnit)

    override fun get(key: String): String? = valueOperations.get(key)

    override fun <HV : Any> hset(key: String, field: String, value: HV) =
        redisTemplate.opsForHash<String, HV>().put(key, field, value)

    override fun <HV : Any> hsetall(key: String, map: Map<String, HV>) =
        redisTemplate.opsForHash<String, HV>().putAll(key, map)

    override fun <HV : Any> hget(key: String, field: String): HV? =
        redisTemplate.opsForHash<String, HV>().get(key, field)

    override fun <HV : Any> hdel(key: String, vararg fields: String): Long =
        redisTemplate.opsForHash<String, HV>().delete(key, fields)

    override fun <HV : Any> hgetall(key: String): Map<String, HV> = redisTemplate.opsForHash<String, HV>().entries(key)

    override fun <HK : Any, HV : Any> hsetType(key: String, field: HK, value: HV) =
        redisTemplate.opsForHash<HK, HV>().put(key, field, value)

    override fun <HK : Any, HV : Any> hsetallType(key: String, map: Map<HK, HV>) =
        redisTemplate.opsForHash<HK, HV>().putAll(key, map)

    override fun <HK : Any, HV : Any> hgetType(key: String, field: HK): HV? =
        redisTemplate.opsForHash<HK, HV>().get(key, field)

    override fun <HK : Any, HV : Any> hdelType(key: String, vararg fields: HK): Long =
        redisTemplate.opsForHash<HK, HV>().delete(key, fields)

    override fun <HK : Any, HV : Any> hgetallType(key: String): Map<HK, HV> =
        redisTemplate.opsForHash<HK, HV>().entries(key)

    override fun lpush(key: String, value: String): Long? = listOperations.leftPush(key, value)

    override fun lpushAll(key: String, vararg value: String): Long? = listOperations.leftPushAll(key, *value)

    override fun lpushAll(key: String, value: Collection<String>): Long? = listOperations.leftPushAll(key, value)

    override fun lpop(key: String): String? = listOperations.leftPop(key)

    override fun llen(key: String): Long? = listOperations.size(key)

    override fun lset(key: String, index: Long, value: String) = listOperations.set(key, index, value)

    override fun lindex(key: String, index: Long): String? = listOperations.index(key, index)

    override fun ltrim(key: String, start: Long, end: Long) = listOperations.trim(key, start, end)

    override fun lrange(key: String, start: Long, end: Long): MutableList<String>? =
        listOperations.range(key, start, end)

    override fun rpush(key: String, value: String): Long? = listOperations.rightPush(key, value)

    override fun rpushAll(key: String, vararg value: String): Long? = listOperations.rightPushAll(key, *value)

    override fun rpushAll(key: String, value: Collection<String>): Long? = listOperations.rightPushAll(key, value)

    override fun rpop(key: String): String? = listOperations.rightPop(key)
}