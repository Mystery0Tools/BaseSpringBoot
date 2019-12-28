package vip.mystery0.base.springboot.model

import vip.mystery0.base.springboot.config.BaseApplicationContext
import vip.mystery0.base.springboot.config.result.ErrorResponseMessage
import vip.mystery0.base.springboot.service.ResponseMessageService
import vip.mystery0.tools.kotlin.factory.ResponseFactory.failure
import vip.mystery0.tools.kotlin.model.Response

/**
 * @author mystery0
 */
class ServiceApiException : RuntimeException {
    var response: Response<*>
        private set

    constructor(response: Response<*>) {
        this.response = response
    }

    constructor(message: ErrorResponseMessage, vararg params: Any) {
        val responseMessageService = BaseApplicationContext.getBean(
            ResponseMessageService::class.java
        )
        var msg = responseMessageService.getTranslate(message.key, message.message)
        msg = responseMessageService.fillTemplate(msg, *params)
        response = failure(message.errorCode, msg)
    }
}