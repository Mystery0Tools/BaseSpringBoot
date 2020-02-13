package vip.mystery0.base.springboot.model

import org.junit.Test
import vip.mystery0.base.springboot.service.ResponseMessageService
import vip.mystery0.tools.kotlin.factory.failure

/**
 * @author mystery0
 * Create at 2020/2/13
 */
class ServiceApiExceptionTest {
    @Test
    fun test() {
        val responseMessageService = ResponseMessageService()
        val test1 = responseMessageService.fillTemplate("param({}) is invalid", "param")
        val test2 = responseMessageService.fillTemplate("param({}) is null", "param")
        println(failure(test1))
        println(failure(test2))
    }
}