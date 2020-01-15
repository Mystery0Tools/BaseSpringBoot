package vip.mystery0.base.springboot.service

import com.fasterxml.jackson.core.type.TypeReference
import org.junit.Test
import vip.mystery0.base.springboot.model.TestUser

/**
 * @author mystery0
 * Create at 2020/1/14
 */
class RestPlusServiceTest {
    private val restPlusService = RestPlusService.Proxy.createByTest()

    @Test
    fun get() {
        val list: List<TestUser>? = restPlusService.get(
            "http://172.20.35.247/mock/447/obj",
            type = object : TypeReference<List<TestUser?>?>() {}.type
        )
        println(list)
        val response: String? = restPlusService.get("http://172.20.35.247/mock/447/null", String::class.java)
        println(response)
    }

    @Test
    fun post() {
        val list1: List<TestUser>? = restPlusService.post(
            "http://172.20.35.247/mock/447/obj",
            object : TypeReference<List<TestUser?>?>() {}.type,
            null
        )
        println(list1)
        val list2: List<TestUser>? = restPlusService.post(
            "http://172.20.35.247/mock/447/obj/with",
            object : TypeReference<List<TestUser?>?>() {}.type,
            list1
        )
        println(list2)
        val response: String? = restPlusService.post(
            "http://172.20.35.247/mock/447/null",
            String::class.java, null
        )
        println(response)
    }

    @Test
    fun put() {
        val list1: List<TestUser>? = restPlusService.put(
            "http://172.20.35.247/mock/447/obj",
            object : TypeReference<List<TestUser?>?>() {}.type,
            null
        )
        println(list1)
        val list2: List<TestUser>? = restPlusService.put(
            "http://172.20.35.247/mock/447/obj/with",
            object : TypeReference<List<TestUser?>?>() {}.type,
            list1
        )
        println(list2)
        val response: String? = restPlusService.put(
            "http://172.20.35.247/mock/447/null",
            String::class.java, null
        )
        println(response)
    }

    @Test
    fun delete() {
        val list1: List<TestUser>? = restPlusService.delete(
            "http://172.20.35.247/mock/447/obj",
            object : TypeReference<List<TestUser?>?>() {}.type,
            null
        )
        println(list1)
        val list2: List<TestUser>? = restPlusService.delete(
            "http://172.20.35.247/mock/447/obj/with",
            object : TypeReference<List<TestUser?>?>() {}.type,
            list1
        )
        println(list2)
        val response: String? = restPlusService.delete(
            "http://172.20.35.247/mock/447/null",
            String::class.java, null
        )
        println(response)
    }
}