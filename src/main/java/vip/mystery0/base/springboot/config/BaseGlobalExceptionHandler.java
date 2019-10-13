package vip.mystery0.base.springboot.config;

import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import vip.mystery0.tools.java.utils.IPUtil;
import vip.mystery0.tools.java.utils.TimeUtil;
import vip.mystery0.tools.kotlin.factory.ResponseFactory;
import vip.mystery0.tools.kotlin.model.Response;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestControllerAdvice
public abstract class BaseGlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BaseGlobalExceptionHandler.class);

    @Autowired
    private PropertiesConfig propertiesConfig;

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
        List<String> params = new ArrayList<>();
        request.getParameterMap().forEach((s, strings) -> {
            String value = StringUtils.join(strings);
            if (value.length() > propertiesConfig.getLogMaxLength()) {
                String start = value.substring(0, 4);
                String end = value.substring(value.length() - 4);
                params.add(s + "=>" + start + "...." + end);
            } else {
                params.add(s + "=>" + value);
            }
        });
        String args = StringUtils.join(params);

        log.info("╔══════════════════════");
        log.info("║ " + TimeUtil.toDateTimeString(Calendar.getInstance()));
        log.info("║ " + request.getMethod() + " " + request.getRequestURI());
        if (!"".equals(args)) {
            log.info("║ params: 【" + params + "】");
        }
        log.info("║ IP: " + IPUtil.getClientIP(request));
        log.info("╙──────────────────────");
        return ResponseFactory.failure(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseFactory.failure(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response<Object> defaultErrorHandler(Exception e) {
        log.error("", e);
        return ResponseFactory.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
