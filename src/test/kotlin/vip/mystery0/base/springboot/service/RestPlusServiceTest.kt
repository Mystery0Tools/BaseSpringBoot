package vip.mystery0.base.springboot.service

import com.fasterxml.jackson.core.type.TypeReference
import org.junit.Test
import org.slf4j.LoggerFactory
import vip.mystery0.base.springboot.model.TestUser

/**
 * @author mystery0
 * Create at 2020/1/14
 */
class RestPlusServiceTest {
    private val logger = LoggerFactory.getLogger(RestPlusServiceTest::class.java)
    private val restPlusService = RestPlusService()

    @Test
    fun get() {
        val list: List<TestUser>? = restPlusService.get(
            "http://172.20.35.247/mock/447/obj",
            type = object : TypeReference<List<TestUser?>?>() {}.type
        )
        logger.info("{}", list)
        val response: String? = restPlusService.get("http://172.20.35.247/mock/447/null", String::class.java)
        logger.info("{}", response)
    }

    @Test
    fun post() {
        val list1: List<TestUser>? = restPlusService.post(
            "http://172.20.35.247/mock/447/obj",
            object : TypeReference<List<TestUser?>?>() {}.type,
            null
        )
        logger.info("{}", list1)
        val list2: List<TestUser>? = restPlusService.post(
            "http://172.20.35.247/mock/447/obj/with",
            object : TypeReference<List<TestUser?>?>() {}.type,
            list1
        )
        logger.info("{}", list2)
        val response: String? = restPlusService.post(
            "http://172.20.35.247/mock/447/null",
            String::class.java, null
        )
        logger.info("{}", response)
    }

    @Test
    fun put() {
        val list1: List<TestUser>? = restPlusService.put(
            "http://172.20.35.247/mock/447/obj",
            object : TypeReference<List<TestUser?>?>() {}.type,
            null
        )
        logger.info("{}", list1)
        val list2: List<TestUser>? = restPlusService.put(
            "http://172.20.35.247/mock/447/obj/with",
            object : TypeReference<List<TestUser?>?>() {}.type,
            list1
        )
        logger.info("{}", list2)
        val response: String? = restPlusService.put(
            "http://172.20.35.247/mock/447/null",
            String::class.java, null
        )
        logger.info("{}", response)
    }

    @Test
    fun delete() {
        val list1: List<TestUser>? = restPlusService.delete(
            "http://172.20.35.247/mock/447/obj",
            object : TypeReference<List<TestUser?>?>() {}.type,
            null
        )
        logger.info("{}", list1)
        val list2: List<TestUser>? = restPlusService.delete(
            "http://172.20.35.247/mock/447/obj/with",
            object : TypeReference<List<TestUser?>?>() {}.type,
            list1
        )
        logger.info("{}", list2)
        val response: String? = restPlusService.delete(
            "http://172.20.35.247/mock/447/null",
            String::class.java, null
        )
        logger.info("{}", response)
    }
}