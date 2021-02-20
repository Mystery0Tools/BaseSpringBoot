package vip.mystery0.base.springboot.config.error

import com.ainemo.http.plus.api.model.ExceptionEntity
import org.springframework.http.HttpStatus
import vip.mystery0.base.springboot.config.factory.ErrorResponseMessageFactory.fillTemplate
import vip.mystery0.base.springboot.config.factory.ErrorResponseMessageFactory.getTranslationOrDefault
import vip.mystery0.base.springboot.config.result.ErrorResponseMessage

/**
 * 包装的业务逻辑异常类
 *
 * @author mystery0
 * @date 2021/01/15
 */
class Alert private constructor(
    val exceptionEntity: ExceptionEntity,
    val httpStatus: HttpStatus,
    val errorMessage: ErrorResponseMessage
) : RuntimeException() {
    override val message: String
        get() = exceptionEntity.developerMessage!!

    companion object {
        fun describe(errorMessage: ErrorResponseMessage, vararg params: String): Alert {
            return Alert(newExceptionInstance(errorMessage, *params), errorMessage.status, errorMessage)
        }

        fun newExceptionInstance(errorMessage: ErrorResponseMessage, vararg params: String): ExceptionEntity {
            return ExceptionEntity(
                errorMessage.errorCode,
                fillTemplate(errorMessage.message, *params),
                getTranslationOrDefault(errorMessage.key, errorMessage.message),
                null
            )
        }
    }
}