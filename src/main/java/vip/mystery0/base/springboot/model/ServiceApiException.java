package vip.mystery0.base.springboot.model;

import vip.mystery0.tools.kotlin.model.Response;

/**
 * @author mystery0
 */
public class ServiceApiException extends RuntimeException {
    private Response<?> response;

    public ServiceApiException(Response<?> response) {
        this.response = response;
    }

    public Response<?> getResponse() {
        return response;
    }
}
