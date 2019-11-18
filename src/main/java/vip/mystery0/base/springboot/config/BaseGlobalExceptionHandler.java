package vip.mystery0.base.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import vip.mystery0.tools.kotlin.factory.ResponseFactory;
import vip.mystery0.tools.kotlin.model.Response;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * @author mystery0
 */
@RestControllerAdvice
public abstract class BaseGlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BaseGlobalExceptionHandler.class);

    @Autowired
    private BaseProperties properties;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response<Object> handleValidationException() {
        return ResponseFactory.failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<Object> handleMethodArgumentTypeMismatchException() {
        return ResponseFactory.failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<Object> handleMissingServletRequestParameterException() {
        return ResponseFactory.failure(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response<Object> handleNoHandlerFoundException(HttpServletRequest request) {
        WebLogAspect.logRequest(request, log, properties.getLogMaxLength());
        return ResponseFactory.failure(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Object> handleHttpRequestMethodNotSupportedException() {
        return ResponseFactory.failure(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Response<Object> handleBindException(BindException e) {
        return ResponseFactory.failure(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
