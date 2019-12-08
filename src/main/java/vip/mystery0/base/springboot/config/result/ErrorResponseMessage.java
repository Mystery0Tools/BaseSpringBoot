package vip.mystery0.base.springboot.config.result;

import org.springframework.http.HttpStatus;

/**
 * @author mystery0
 * Create at 2019/12/8
 */
public interface ErrorResponseMessage {
    default String getMessage() {
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    default int getErrorCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    String getPropertiesFileName();

    String getKey();
}
