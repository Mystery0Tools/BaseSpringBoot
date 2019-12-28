package vip.mystery0.base.springboot.config.result

import org.springframework.http.HttpStatus

/**
 * @author mystery0
 * Create at 2019/12/8
 */
interface ErrorResponseMessage {
    val message: String
        get() = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase

    val errorCode: Int
        get() = HttpStatus.INTERNAL_SERVER_ERROR.value()

    val key: String
}