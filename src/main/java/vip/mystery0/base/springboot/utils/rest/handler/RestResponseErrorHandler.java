package vip.mystery0.base.springboot.utils.rest.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @author mystery0
 * @date 2019/11/18
 */
public class RestResponseErrorHandler implements ResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(RestResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() != HttpStatus.OK) {
            log.warn("Status code: {} , text {}", response.getStatusCode(), response.getStatusText());
            log.warn("Response: {}", response.getBody());
            return true;
        }
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) {
    }
}
