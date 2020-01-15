package vip.mystery0.base.springboot.utils.rest

import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.converter.GenericHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.client.*
import java.lang.reflect.Type

/**
 * @author mystery0
 * Create at 2019/12/25
 */
open class RestTemplatePlus<EXCEPTION>(
    private val exceptionClass: Class<EXCEPTION>,
    private val handler: (EXCEPTION) -> Unit,
    private val json: JSON,
    val restTemplate: RestTemplate
) {
    fun <T> get(
        url: String,
        type: Type = Void::class.java,
        mapper: ((EXCEPTION) -> T)? = null,
        vararg uriVariables: Any
    ): T? = doRequestWithType(url, HttpMethod.GET, null, type, mapper, uriVariables)

    fun <T> post(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        mapper: ((EXCEPTION) -> T)? = null,
        vararg uriVariables: Any
    ): T? = doRequestWithType(url, HttpMethod.POST, request, type, mapper, uriVariables)

    fun <T> put(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        mapper: ((EXCEPTION) -> T)? = null,
        vararg uriVariables: Any
    ): T? = doRequestWithType(url, HttpMethod.PUT, request, type, mapper, uriVariables)

    fun <T> delete(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        mapper: ((EXCEPTION) -> T)? = null,
        vararg uriVariables: Any
    ): T? = doRequestWithType(url, HttpMethod.DELETE, request, type, mapper, uriVariables)

    private fun <T> doRequestWithType(
        url: String,
        httpMethod: HttpMethod,
        request: Any?,
        type: Type,
        mapper: ((EXCEPTION) -> T)? = null,
        vararg uriVariables: Any
    ): T? = doRequest(
        url,
        httpMethod,
        request,
        type,
        mapper,
        { json.fromJson(it, type) },
        uriVariables
    )

    @Suppress("unchecked_cast")
    private fun <T> doRequest(
        url: String,
        httpMethod: HttpMethod,
        request: Any?,
        responseType: Type,
        mapper: ((EXCEPTION) -> T)? = null,
        data: (String) -> T,
        vararg uriVariables: Any
    ): T? {
        val requestCallback = httpEntityCallback(request, String::class.java)
        val responseExtractor = responseEntityExtractor<String>(String::class.java)
        val responseEntity =
            restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables) ?: return null
        when (responseEntity.statusCode) {
            HttpStatus.OK -> {
                if (responseEntity.body.isNullOrEmpty() || responseType == Void::class.java)
                    return null
                if (responseType == String::class.java)
                    return responseEntity.body as T?
                return if (responseEntity.body == null) null else data(responseEntity.body!!)
            }
            HttpStatus.NO_CONTENT -> return null
            else -> {
                val exception =
                    json.fromJson(responseEntity.body!!, exceptionClass)
                mapper?.let { it(exception) }
                handler(exception)
                throw RuntimeException("you must throw exception in handler")
            }
        }
    }

    /**
     * Returns a request callback implementation that writes the given object to the
     * request stream.
     */
    protected open fun httpEntityCallback(requestBody: Any?, responseType: Type): RequestCallback =
        HttpEntityRequestCallback(requestBody, responseType)

    /**
     * Returns a response extractor for [ResponseEntity].
     */
    protected open fun <T> responseEntityExtractor(responseType: Type): ResponseExtractor<ResponseEntity<T>> =
        ResponseEntityResponseExtractor(responseType)

    /**
     * Request callback implementation that prepares the request's accept headers.
     */
    private open inner class AcceptHeaderRequestCallback(private val responseType: Type) : RequestCallback {
        override fun doWithRequest(request: ClientHttpRequest) {
            val responseClass: Class<*>? = if (responseType is Class<*>) responseType else null
            val allSupportedMediaTypes = ArrayList<MediaType>()
            restTemplate.messageConverters.forEach { converter ->
                when {
                    responseClass != null -> {
                        if (converter.canRead(responseClass, null))
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter))
                    }
                    converter is GenericHttpMessageConverter -> {
                        if (converter.canRead(responseType, null, null))
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter))
                    }
                }
            }
            if (allSupportedMediaTypes.isEmpty()) {
                MediaType.sortBySpecificity(allSupportedMediaTypes)
                request.headers.accept = allSupportedMediaTypes
            }
        }

        private fun getSupportedMediaTypes(messageConverter: HttpMessageConverter<*>): List<MediaType> {
            val supportedMediaTypes = messageConverter.supportedMediaTypes
            return supportedMediaTypes.map { supportedMediaType ->
                if (supportedMediaType.charset != null)
                    MediaType(supportedMediaType.type, supportedMediaType.subtype)
                else
                    supportedMediaType
            }
        }
    }

    /**
     * Request callback implementation that writes the given object to the request stream.
     */
    private inner class HttpEntityRequestCallback(
        requestBody: Any?,
        responseType: Type
    ) : AcceptHeaderRequestCallback(responseType) {
        private val requestEntity = when {
            requestBody is HttpEntity<*> -> requestBody
            requestBody != null -> HttpEntity(requestBody)
            else -> HttpEntity.EMPTY
        }

        @Suppress("unchecked_cast")
        override fun doWithRequest(request: ClientHttpRequest) {
            super.doWithRequest(request)
            if (!requestEntity.hasBody()) {
                val httpHeaders = request.headers
                val requestHeaders = requestEntity.headers
                if (requestHeaders.isNotEmpty())
                    httpHeaders.putAll(requestHeaders)
                if (httpHeaders.contentLength < 0)
                    httpHeaders.contentLength = 0
            } else {
                val requestBody = requestEntity.body
                val requestBodyClass = requestBody!!.javaClass
                val requestBodyType = if (requestEntity is RequestEntity<*>) requestEntity.type else requestBodyClass
                val requestHeaders = requestEntity.headers
                val requestContentType = requestHeaders.contentType
                for (messageConverter in restTemplate.messageConverters) {
                    when {
                        messageConverter is GenericHttpMessageConverter -> {
                            if (messageConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
                                if (requestHeaders.isNotEmpty())
                                    request.headers.putAll(requestHeaders)
                                (messageConverter as GenericHttpMessageConverter<Any>).write(
                                    requestBody,
                                    requestBodyType,
                                    requestContentType,
                                    request
                                )
                                return
                            }
                        }
                        messageConverter.canWrite(requestBodyClass, requestContentType) -> {
                            if (requestHeaders.isNotEmpty())
                                request.headers.putAll(requestHeaders)
                            (messageConverter as HttpMessageConverter<Any>).write(
                                requestBody,
                                requestContentType,
                                request
                            )
                            return
                        }
                    }
                }
                var message =
                    "Could not write request: no suitable HttpMessageConverter found for request type [${requestBodyClass.name}]"
                if (requestContentType != null)
                    message += " and content type [$requestContentType]"
                throw RestClientException(message)
            }
        }
    }

    /**
     * Response extractor for [HttpEntity].
     */
    private inner class ResponseEntityResponseExtractor<T>(responseType: Type) : ResponseExtractor<ResponseEntity<T>> {
        private var delegate: HttpMessageConverterExtractor<T>? =
            if (responseType != Void::class.java) HttpMessageConverterExtractor(
                responseType,
                restTemplate.messageConverters
            )
            else null

        override fun extractData(response: ClientHttpResponse): ResponseEntity<T> {
            return if (delegate != null) {
                val body = delegate!!.extractData(response)
                ResponseEntity(body, response.headers, response.statusCode)
            } else
                ResponseEntity(response.headers, response.statusCode)
        }
    }
}