package vip.mystery0.base.springboot.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import vip.mystery0.base.springboot.model.ServiceApiException
import vip.mystery0.base.springboot.utils.trace.TraceHelper.endTrace
import vip.mystery0.base.springboot.utils.trace.TraceLogUtil.logRequest
import vip.mystery0.tools.kotlin.factory.ResponseFactory.failure
import vip.mystery0.tools.kotlin.model.Response
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException
import javax.validation.ValidationException

/**
 * @author mystery0
 */
@RestControllerAdvice
abstract class BaseGlobalExceptionHandler {
    @Autowired
    private val properties: BaseProperties? = null

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(exception: ValidationException): Response<*> {
        var errorMessage: String? = null
        if (exception is ConstraintViolationException) {
            for (cv in exception.constraintViolations) {
                errorMessage = cv.message
                break
            }
        } else {
            errorMessage = exception.message
        }
        return response(HttpStatus.BAD_REQUEST.value(), errorMessage)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(): Response<*> =
        response(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.reasonPhrase)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(): Response<*> =
        response(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.reasonPhrase)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(request: HttpServletRequest?): Response<*> {
        logRequest(request!!, properties!!.logMaxLength)
        return response(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.reasonPhrase)
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(): Response<*> =
        response(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.reasonPhrase)

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): Response<*> =
        response(HttpStatus.BAD_REQUEST.value(), e.bindingResult.allErrors[0].defaultMessage)

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceApiException::class)
    fun handleServiceApiException(e: ServiceApiException): Response<*> {
        endTrace()
        return e.response
    }

    private fun response(code: Int, message: String?): Response<*> {
        endTrace()
        return failure(code, message)
    }
}