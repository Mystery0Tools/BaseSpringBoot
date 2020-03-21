package vip.mystery0.base.springboot.config.redis

import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * @author mystery0
 * Create at 2020/3/21
 */
interface IRedis {
    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     */
    fun ttl(key: String): Long

    /**
     * 实现命令：expire 设置过期时间，单位毫秒
     */
    fun expire(key: String, timeout: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Boolean

    /**
     * 实现命令：INCR key，增加key一次
     */
    fun incr(key: String): Long?

    /**
     * 实现命令：INCR key，增加key delta次
     */
    fun incr(key: String, delta: Long): Long?

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    fun keys(pattern: String): Set<String>

    /**
     * 实现命令：DEL key，删除一个key
     */
    fun del(key: String): Boolean

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     */
    fun set(key: String, value: String)

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（毫秒）
     */
    fun set(key: String, value: String, timeout: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS)

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     */
    fun get(key: String): String?

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     */
    fun <HV : Any> hset(key: String, field: String, value: HV)

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     */
    fun <HV : Any> hsetall(key: String, map: Map<String, HV>)

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     */
    fun <HV : Any> hget(key: String, field: String): HV?

    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     */
    fun <HV : Any> hdel(key: String, vararg fields: String): Long

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     */
    fun <HV : Any> hgetall(key: String): Map<String, HV>

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     */
    fun <HK : Any, HV : Any> hsetType(key: String, field: HK, value: HV)

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     */
    fun <HK : Any, HV : Any> hsetallType(key: String, map: Map<HK, HV>)

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     */
    fun <HK : Any, HV : Any> hgetType(key: String, field: HK): HV?

    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     */
    fun <HK : Any, HV : Any> hdelType(key: String, vararg fields: HK): Long

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     */
    fun <HK : Any, HV : Any> hgetallType(key: String): Map<HK, HV>

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     */
    fun lpush(key: String, value: String): Long?

    /**
     * 实现命令：LPUSH key value，将多个值 value插入到列表 key的表头
     */
    fun lpushAll(key: String, vararg value: String): Long?

    /**
     * 实现命令：LPUSH key value，将多个值 value插入到列表 key的表头
     */
    fun lpushAll(key: String, value: Collection<String>): Long?

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     */
    fun lpop(key: String): String?

    /**
     * 实现命令：LLEN key，获取列表长度
     */
    fun llen(key: String): Long?

    /**
     * 实现命令：LRANGE key start stop，获取列表指定范围内的元素
     */
    fun lrange(key: String, start: Long, end: Long): MutableList<String>?

    /**
     * 实现命令：LINDEX key index，通过索引获取列表中的元素
     */
    fun lindex(key: String, index: Long): String?

    /**
     * 实现命令：LSET key index value，通过索引设置列表元素的值
     */
    fun lset(key: String, index: Long, value: String)

    /**
     * 实现命令：LTRIM key start stop，对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     */
    fun ltrim(key: String, start: Long, end: Long)

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     */
    fun rpush(key: String, value: String): Long?

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     */
    fun rpushAll(key: String, vararg value: String): Long?

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     */
    fun rpushAll(key: String, value: Collection<String>): Long?

    /**
     * 实现命令：RPOP key，移除并返回列表 key的尾元素。
     */
    fun rpop(key: String): String?
}