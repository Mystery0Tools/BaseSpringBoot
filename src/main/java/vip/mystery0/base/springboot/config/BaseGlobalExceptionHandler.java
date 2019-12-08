package vip.mystery0.base.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import vip.mystery0.base.springboot.model.ServiceApiException;
import vip.mystery0.base.springboot.utils.trace.TraceHelper;
import vip.mystery0.base.springboot.utils.trace.TraceLogUtil;
import vip.mystery0.tools.kotlin.factory.ResponseFactory;
import vip.mystery0.tools.kotlin.model.Response;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * @author mystery0
 */
@RestControllerAdvice
public abstract class BaseGlobalExceptionHandler {
    @Autowired
    private BaseProperties properties;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response<?> handleValidationException(ValidationException exception) {
        String errorMessage = null;
        if (exception instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> cv : ((ConstraintViolationException) exception).getConstraintViolations()) {
                errorMessage = cv.getMessage();
                break;
            }
        } else {
            errorMessage = exception.getMessage();
        }
        return response(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<?> handleMethodArgumentTypeMismatchException() {
        return response(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<?> handleMissingServletRequestParameterException() {
        return response(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response<?> handleNoHandlerFoundException(HttpServletRequest request) {
        TraceLogUtil.logRequest(request, properties.getLogMaxLength());
        return response(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<?> handleHttpRequestMethodNotSupportedException() {
        return response(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Response<?> handleBindException(BindException e) {
        return response(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceApiException.class)
    public Response<?> handleServiceApiException(ServiceApiException e) {
        TraceHelper.endTrace();
        return e.getResponse();
    }

    private Response<?> response(int code, String message) {
        TraceHelper.endTrace();
        return ResponseFactory.failure(code, message);
    }
}
