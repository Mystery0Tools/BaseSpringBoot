package vip.mystery0.base.springboot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vip.mystery0.base.springboot.model.ServiceApiException;
import vip.mystery0.base.springboot.utils.rest.JSON;
import vip.mystery0.base.springboot.utils.rest.RestTemplatePlus;
import vip.mystery0.base.springboot.utils.rest.handler.RestResponseErrorHandler;
import vip.mystery0.base.springboot.utils.rest.interceptor.LoggingClientHttpRequestInterceptor;
import vip.mystery0.tools.java.factory.JsonFactory;
import vip.mystery0.tools.kotlin.model.Response;

/**
 * @author Mystery0
 */
@Service
public class RestPlusService {
    private RestTemplatePlus<Response> restTemplatePlus;

    public RestPlusService() {
        restTemplatePlus = new RestTemplatePlus<>(createRestTemplate(), e -> {
            throw new ServiceApiException(e);
        }, new JSON() {
            @Override
            public <T> T fromJson(String json, Class<T> clazz) {
                return JsonFactory.fromJson(json, clazz);
            }

            @Override
            public <T> T fromJson(String json, TypeReference<T> typeReference) {
                return JsonFactory.fromJson(json, typeReference);
            }

            @Override
            public <T> String toJson(T obj) {
                return JsonFactory.toJson(obj);
            }
        });
    }

    private static RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        return restTemplate;
    }

    public void get(String url,
                    Object... uriVariables) {
        restTemplatePlus.get(url, Void.class, uriVariables);
    }

    public <T> T get(String url,
                     Class<T> clazz,
                     Object... uriVariables) {
        return restTemplatePlus.get(url, clazz, uriVariables);
    }

    public <T> T get(String url,
                     TypeReference<T> typeReference,
                     Object... uriVariables) {
        return restTemplatePlus.get(url, typeReference, uriVariables);
    }

    public void post(String url,
                     Object... uriVariables) {
        restTemplatePlus.post(url, null, Void.class, uriVariables);
    }

    public void post(String url,
                     Object request,
                     Object... uriVariables) {
        restTemplatePlus.post(url, request, Void.class, uriVariables);
    }

    public <T> T post(String url,
                      Class<T> clazz,
                      Object... uriVariables) {
        return restTemplatePlus.post(url, null, clazz, uriVariables);
    }

    public <T> T post(String url,
                      Class<T> clazz,
                      Object request,
                      Object... uriVariables) {
        return restTemplatePlus.post(url, request, clazz, uriVariables);
    }

    public <T> T post(String url,
                      TypeReference<T> typeReference,
                      Object... uriVariables) {
        return restTemplatePlus.post(url, null, typeReference, uriVariables);
    }

    public <T> T post(String url,
                      TypeReference<T> typeReference,
                      Object request,
                      Object... uriVariables) {
        return restTemplatePlus.post(url, request, typeReference, uriVariables);
    }

    public void put(String url,
                    Object... uriVariables) {
        restTemplatePlus.put(url, null, Void.class, uriVariables);
    }

    public void put(String url,
                    Object request,
                    Object... uriVariables) {
        restTemplatePlus.put(url, request, Void.class, uriVariables);
    }

    public <T> T put(String url,
                     Class<T> clazz,
                     Object... uriVariables) {
        return restTemplatePlus.put(url, null, clazz, uriVariables);
    }

    public <T> T put(String url,
                     Class<T> clazz,
                     Object request,
                     Object... uriVariables) {
        return restTemplatePlus.put(url, request, clazz, uriVariables);
    }

    public <T> T put(String url,
                     TypeReference<T> typeReference,
                     Object... uriVariables) {
        return restTemplatePlus.put(url, null, typeReference, uriVariables);
    }

    public <T> T put(String url,
                     TypeReference<T> typeReference,
                     Object request,
                     Object... uriVariables) {
        return restTemplatePlus.put(url, request, typeReference, uriVariables);
    }

    public void delete(String url,
                       Object... uriVariables) {
        restTemplatePlus.delete(url, null, Void.class, uriVariables);
    }

    public void delete(String url,
                       Object request,
                       Object... uriVariables) {
        restTemplatePlus.delete(url, request, Void.class, uriVariables);
    }

    public <T> T delete(String url,
                        Class<T> clazz,
                        Object... uriVariables) {
        return restTemplatePlus.delete(url, null, clazz, uriVariables);
    }

    public <T> T delete(String url,
                        Class<T> clazz,
                        Object request,
                        Object... uriVariables) {
        return restTemplatePlus.delete(url, request, clazz, uriVariables);
    }

    public <T> T delete(String url,
                        TypeReference<T> typeReference,
                        Object... uriVariables) {
        return restTemplatePlus.delete(url, null, typeReference, uriVariables);
    }

    public <T> T delete(String url,
                        TypeReference<T> typeReference,
                        Object request,
                        Object... uriVariables) {
        return restTemplatePlus.delete(url, request, typeReference, uriVariables);
    }
}
