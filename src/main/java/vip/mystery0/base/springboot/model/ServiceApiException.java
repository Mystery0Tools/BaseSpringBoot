package vip.mystery0.base.springboot.model;

import vip.mystery0.tools.kotlin.model.Response;

/**
 * @author mystery0
 * @date 2019/11/18
 */
public class ServiceApiException extends RuntimeException {
    private Response response;

    public ServiceApiException(Response response) {
        this.response = response;
    }
}
