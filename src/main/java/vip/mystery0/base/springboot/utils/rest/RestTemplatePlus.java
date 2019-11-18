package vip.mystery0.base.springboot.utils.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 邓易林
 * @date 2019/11/15
 */
public class RestTemplatePlus<EXCEPTION> {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplatePlus.class);

    private RestTemplate restTemplate;
    private Consumer<EXCEPTION> handler;
    private JSON json;

    public RestTemplatePlus(Consumer<EXCEPTION> handler, JSON json) {
        this.restTemplate = new RestTemplate();
        this.handler = handler;
        this.json = json;
    }

    public RestTemplatePlus(RestTemplate restTemplate,
                            Consumer<EXCEPTION> handler,
                            JSON json) {
        this.restTemplate = restTemplate;
        this.handler = handler;
        this.json = json;
    }

    public <T> T get(String url,
                     Class<T> clazz,
                     Object... uriVariables) {
        return doRequest(url, HttpMethod.GET, null, clazz, null, uriVariables);
    }

    public <T> T get(String url,
                     Class<T> clazz,
                     Function<EXCEPTION, T> mapper,
                     Object... uriVariables) {
        return doRequest(url, HttpMethod.GET, null, clazz, mapper, uriVariables);
    }

    public <T> T get(String url,
                     TypeReference<T> typeReference,
                     Object... uriVariables) {
        return doRequestByType(url, HttpMethod.GET, null, typeReference, null, uriVariables);
    }

    public <T> T get(String url,
                     TypeReference<T> typeReference,
                     Function<EXCEPTION, T> mapper,
                     Object... uriVariables) {
        return doRequestByType(url, HttpMethod.GET, null, typeReference, mapper, uriVariables);
    }

    public <T> T post(String url,
                      Object request,
                      Class<T> clazz,
                      Object... uriVariables) {
        return doRequest(url, HttpMethod.POST, request, clazz, null, uriVariables);
    }

    public <T> T post(String url,
                      Object request,
                      Class<T> clazz,
                      Function<EXCEPTION, T> mapper,
                      Object... uriVariables) {
        return doRequest(url, HttpMethod.POST, request, clazz, mapper, uriVariables);
    }

    public <T> T post(String url,
                      Object request,
                      TypeReference<T> typeReference,
                      Object... uriVariables) {
        return doRequestByType(url, HttpMethod.POST, request, typeReference, null, uriVariables);
    }

    public <T> T post(String url,
                      Object request,
                      TypeReference<T> typeReference,
                      Function<EXCEPTION, T> mapper,
                      Object... uriVariables) {
        return doRequestByType(url, HttpMethod.POST, request, typeReference, mapper, uriVariables);
    }

    public <T> T put(String url,
                     Object request,
                     Class<T> clazz,
                     Object... uriVariables) {
        return doRequest(url, HttpMethod.PUT, request, clazz, null, uriVariables);
    }

    public <T> T put(String url,
                     Object request,
                     Class<T> clazz,
                     Function<EXCEPTION, T> mapper,
                     Object... uriVariables) {
        return doRequest(url, HttpMethod.PUT, request, clazz, mapper, uriVariables);
    }

    public <T> T put(String url,
                     Object request,
                     TypeReference<T> typeReference,
                     Object... uriVariables) {
        return doRequestByType(url, HttpMethod.PUT, request, typeReference, null, uriVariables);
    }

    public <T> T put(String url,
                     Object request,
                     TypeReference<T> typeReference,
                     Function<EXCEPTION, T> mapper,
                     Object... uriVariables) {
        return doRequestByType(url, HttpMethod.PUT, request, typeReference, mapper, uriVariables);
    }

    public <T> T delete(String url,
                        Object request,
                        Class<T> clazz,
                        Object... uriVariables) {
        return doRequest(url, HttpMethod.DELETE, request, clazz, null, uriVariables);
    }

    public <T> T delete(String url,
                        Object request,
                        Class<T> clazz,
                        Function<EXCEPTION, T> mapper,
                        Object... uriVariables) {
        return doRequest(url, HttpMethod.DELETE, request, clazz, mapper, uriVariables);
    }

    public <T> T delete(String url,
                        Object request,
                        TypeReference<T> typeReference,
                        Object... uriVariables) {
        return doRequestByType(url, HttpMethod.DELETE, request, typeReference, null, uriVariables);
    }

    public <T> T delete(String url,
                        Object request,
                        TypeReference<T> typeReference,
                        Function<EXCEPTION, T> mapper,
                        Object... uriVariables) {
        return doRequestByType(url, HttpMethod.DELETE, request, typeReference, mapper, uriVariables);
    }

    private <T> T doRequest(String url,
                            HttpMethod httpMethod,
                            Object request,
                            Class<T> clazz,
                            Function<EXCEPTION, T> mapper,
                            Object... uriVariables) {
        return doRequestWithClass(url, httpMethod, request, clazz, mapper, uriVariables);
    }

    private <T> T doRequestByType(String url,
                                  HttpMethod httpMethod,
                                  Object request,
                                  TypeReference<T> typeReference,
                                  Function<EXCEPTION, T> mapper,
                                  Object... uriVariables) {
        return doRequestWithType(url, httpMethod, request, typeReference, mapper, uriVariables);
    }

    @SuppressWarnings("unchecked")
    private <T> T doRequestWithClass(String url,
                                     HttpMethod httpMethod,
                                     Object request,
                                     Class<T> responseType,
                                     Function<EXCEPTION, T> mapper,
                                     Object... uriVariables) {
        RequestCallback requestCallback = httpEntityCallback(request, String.class);
        ResponseExtractor<ResponseEntity<String>> responseExtractor = responseEntityExtractor(String.class);
        ResponseEntity<String> responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
        if (responseEntity == null) {
            return null;
        }
        switch (responseEntity.getStatusCode()) {
            case OK:
                if ("".equals(responseEntity.getBody()) || responseType.equals(Void.class)) {
                    return null;
                }
                if (responseType.equals(String.class)) {
                    return (T) responseEntity.getBody();
                }
                return json.fromJson(responseEntity.getBody(), responseType);
            case NO_CONTENT:
                return null;
            default:
                EXCEPTION exception = json.fromJson(responseEntity.getBody(), new TypeReference<EXCEPTION>() {
                });
                if (mapper == null) {
                    handler.accept(exception);
                    throw new RuntimeException("you must throw exception in handler");
                } else {
                    return mapper.apply(exception);
                }
        }
    }

    private <T> T doRequestWithType(String url,
                                    HttpMethod httpMethod,
                                    Object request,
                                    TypeReference<T> typeReference,
                                    Function<EXCEPTION, T> mapper,
                                    Object... uriVariables) {
        RequestCallback requestCallback = httpEntityCallback(request, String.class);
        ResponseExtractor<ResponseEntity<String>> responseExtractor = responseEntityExtractor(String.class);
        ResponseEntity<String> responseEntity = restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
        if (responseEntity == null) {
            return null;
        }
        switch (responseEntity.getStatusCode()) {
            case OK:
                if ("".equals(responseEntity.getBody())) {
                    return null;
                }
                return json.fromJson(responseEntity.getBody(), typeReference);
            case NO_CONTENT:
                return null;
            default:
                EXCEPTION exception = json.fromJson(responseEntity.getBody(), new TypeReference<EXCEPTION>() {
                });
                if (mapper == null) {
                    handler.accept(exception);
                    throw new RuntimeException("you must throw exception in handler");
                } else {
                    return mapper.apply(exception);
                }
        }
    }


    /**
     * Returns a request callback implementation that prepares the request {@code Accept}
     * headers based on the given response type and configured
     * {@linkplain RestTemplate#getMessageConverters() message converters}.
     */
    protected <T> RequestCallback acceptHeaderRequestCallback(Class<T> responseType) {
        return new AcceptHeaderRequestCallback(responseType);
    }

    /**
     * Returns a request callback implementation that writes the given object to the
     * request stream.
     */
    protected <T> RequestCallback httpEntityCallback(Object requestBody) {
        return new HttpEntityRequestCallback(requestBody);
    }

    /**
     * Returns a request callback implementation that writes the given object to the
     * request stream.
     */
    protected <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
        return new HttpEntityRequestCallback(requestBody, responseType);
    }

    /**
     * Returns a response extractor for {@link ResponseEntity}.
     */
    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
        return new ResponseEntityResponseExtractor<T>(responseType);
    }

    /**
     * Request callback implementation that prepares the request's accept headers.
     */
    private class AcceptHeaderRequestCallback implements RequestCallback {

        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                Class<?> responseClass = null;
                if (this.responseType instanceof Class) {
                    responseClass = (Class<?>) this.responseType;
                }
                List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
                for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
                    if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    } else if (converter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                        if (genericConverter.canRead(this.responseType, null, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
                    }
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                if (supportedMediaType.getCharset() != null) {
                    supportedMediaType =
                            new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
                }
                result.add(supportedMediaType);
            }
            return result;
        }
    }

    /**
     * Request callback implementation that writes the given object to the request stream.
     */
    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(Object requestBody) {
            this(requestBody, null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity<?>) requestBody;
            } else if (requestBody != null) {
                this.requestEntity = new HttpEntity<Object>(requestBody);
            } else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            if (!this.requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    httpHeaders.putAll(requestHeaders);
                }
                if (httpHeaders.getContentLength() < 0) {
                    httpHeaders.setContentLength(0L);
                }
            } else {
                Object requestBody = this.requestEntity.getBody();
                Class<?> requestBodyClass = requestBody.getClass();
                Type requestBodyType = (this.requestEntity instanceof RequestEntity ?
                        ((RequestEntity<?>) this.requestEntity).getType() : requestBodyClass);
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                    if (messageConverter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<Object> genericMessageConverter = (GenericHttpMessageConverter<Object>) messageConverter;
                        if (genericMessageConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
                            if (!requestHeaders.isEmpty()) {
                                httpRequest.getHeaders().putAll(requestHeaders);
                            }
                            if (logger.isDebugEnabled()) {
                                if (requestContentType != null) {
                                    logger.debug("Writing [" + requestBody + "] as \"" + requestContentType +
                                            "\" using [" + messageConverter + "]");
                                } else {
                                    logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                                }

                            }
                            genericMessageConverter.write(
                                    requestBody, requestBodyType, requestContentType, httpRequest);
                            return;
                        }
                    } else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        if (logger.isDebugEnabled()) {
                            if (requestContentType != null) {
                                logger.debug("Writing [" + requestBody + "] as \"" + requestContentType +
                                        "\" using [" + messageConverter + "]");
                            } else {
                                logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }

                        }
                        ((HttpMessageConverter<Object>) messageConverter).write(
                                requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "Could not write request: no suitable HttpMessageConverter found for request type [" +
                        requestBodyClass.getName() + "]";
                if (requestContentType != null) {
                    message += " and content type [" + requestContentType + "]";
                }
                throw new RestClientException(message);
            }
        }
    }

    /**
     * Response extractor for {@link HttpEntity}.
     */
    private class ResponseEntityResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {

        private final HttpMessageConverterExtractor<T> delegate;

        public ResponseEntityResponseExtractor(Type responseType) {
            if (responseType != null && Void.class != responseType) {
                this.delegate = new HttpMessageConverterExtractor<T>(responseType, restTemplate.getMessageConverters());
            } else {
                this.delegate = null;
            }
        }

        @Override
        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
            if (this.delegate != null) {
                T body = this.delegate.extractData(response);
                return new ResponseEntity<T>(body, response.getHeaders(), response.getStatusCode());
            } else {
                return new ResponseEntity<T>(response.getHeaders(), response.getStatusCode());
            }
        }
    }

    /**
     * Response extractor that extracts the response {@link HttpHeaders}.
     */
    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {

        @Override
        public HttpHeaders extractData(ClientHttpResponse response) throws IOException {
            return response.getHeaders();
        }
    }
}
