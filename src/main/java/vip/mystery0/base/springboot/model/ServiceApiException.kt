package vip.mystery0.base.springboot.model;

import vip.mystery0.base.springboot.config.BaseApplicationContext;
import vip.mystery0.base.springboot.config.result.ErrorResponseMessage;
import vip.mystery0.base.springboot.service.ResponseMessageService;
import vip.mystery0.tools.kotlin.factory.ResponseFactory;
import vip.mystery0.tools.kotlin.model.Response;

/**
 * @author mystery0
 */
public class ServiceApiException extends RuntimeException {
    private Response<?> response;

    public ServiceApiException(Response<?> response) {
        this.response = response;
    }

    public ServiceApiException(ErrorResponseMessage message, Object... params) {
        ResponseMessageService responseMessageService = BaseApplicationContext.getBean(ResponseMessageService.class);
        String msg = responseMessageService.getTranslate(message.getKey(), message.getMessage());
        msg = responseMessageService.fillTemplate(msg, params);
        response = ResponseFactory.failure(message.getErrorCode(), msg);
    }

    public Response<?> getResponse() {
        return response;
    }
}
