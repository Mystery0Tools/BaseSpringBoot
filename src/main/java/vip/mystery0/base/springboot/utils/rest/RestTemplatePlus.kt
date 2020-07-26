package vip.mystery0.base.springboot.utils.rest

import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.converter.GenericHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.util.MultiValueMap
import org.springframework.web.client.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.reflect.Type

/**
 * @author mystery0
 * Create at 2019/12/25
 */
open class RestTemplatePlus<EXCEPTION>(
    private val exceptionClass: Class<EXCEPTION>,
    private val handler: (HttpStatus, EXCEPTION) -> Unit,
    private val json: JSON,
    val restTemplate: RestTemplate
) {
    fun <T> get(
        url: String,
        type: Type = Void::class.java
    ): T? = doRequest(
        Request.Builder()
            .url(url)
            .method(HttpMethod.GET)
            .response(type)
            .build()
    )

    fun <T> getForEntity(
        url: String,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = doRequestForEntity(
        Request.Builder()
            .url(url)
            .method(HttpMethod.GET)
            .response(type)
            .build()
    )

    fun getFile(request: Request, targetPath: String) = getFile(request, File(targetPath))

    fun getFile(request: Request, file: File) = getFile(request, FileOutputStream(file))

    fun getFile(request: Request, outputStream: OutputStream) {
        //定义请求头接收类型
        request.requestEntity?.headers?.accept = arrayListOf(
            MediaType.APPLICATION_OCTET_STREAM,
            MediaType.ALL
        )
        //对响应进行流式处理而不是全部加载到内存中
        restTemplate.execute(
            request.uri,
            request.httpMethod,
            HttpEntityRequestCallback(request.requestEntity, Void::class.java),
            ResponseExtractor<Void> {
                it.body.copyTo(outputStream)
                it.body.close()
                outputStream.close()
                null
            })
    }

    fun <T> post(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): T? = doRequest(
        Request.Builder()
            .url(url)
            .body(request)
            .method(HttpMethod.POST)
            .response(type)
            .build()
    )

    fun <T> postForEntity(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = doRequestForEntity(
        Request.Builder()
            .url(url)
            .body(request)
            .method(HttpMethod.POST)
            .response(type)
            .build()
    )

    fun <T> postFile(
        url: String,
        map: MultiValueMap<String, Any>? = null,
        file: File,
        mimeType: String,
        type: Type = Void::class.java
    ): T? = doRequest(
        Request.Builder()
            .url(url)
            .fileBody(file, mimeType, map)
            .method(HttpMethod.POST)
            .response(type)
            .build()
    )

    fun <T> postFile(
        url: String,
        map: MultiValueMap<String, Any>? = null,
        byteArray: ByteArray,
        fileName: String,
        mimeType: String,
        type: Type = Void::class.java
    ): T? = doRequest(
        Request.Builder()
            .url(url)
            .fileBody(byteArray, mimeType, fileName, map)
            .method(HttpMethod.POST)
            .response(type)
            .build()
    )

    fun <T> postFileForEntity(
        url: String,
        map: MultiValueMap<String, Any>? = null,
        file: File,
        mimeType: String,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = doRequestForEntity(
        Request.Builder()
            .url(url)
            .fileBody(file, mimeType, map)
            .method(HttpMethod.POST)
            .response(type)
            .build()
    )

    fun <T> postFileForEntity(
        url: String,
        map: MultiValueMap<String, Any>? = null,
        byteArray: ByteArray,
        fileName: String,
        mimeType: String,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = doRequestForEntity(
        Request.Builder()
            .url(url)
            .fileBody(byteArray, mimeType, fileName, map)
            .method(HttpMethod.POST)
            .response(type)
            .build()
    )

    fun <T> put(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): T? = doRequest(
        Request.Builder()
            .url(url)
            .body(request)
            .method(HttpMethod.PUT)
            .response(type)
            .build()
    )

    fun <T> putForEntity(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = doRequestForEntity(
        Request.Builder()
            .url(url)
            .body(request)
            .method(HttpMethod.PUT)
            .response(type)
            .build()
    )

    fun <T> delete(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): T? = doRequest(
        Request.Builder()
            .url(url)
            .body(request)
            .method(HttpMethod.DELETE)
            .response(type)
            .build()
    )

    fun <T> deleteForEntity(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = doRequestForEntity(
        Request.Builder()
            .url(url)
            .body(request)
            .method(HttpMethod.DELETE)
            .response(type)
            .build()
    )

    @Suppress("unchecked_cast")
    fun <T> doRequest(request: Request): T? {
        try {
            val responseEntity = restTemplate.exchange(
                request.uri,
                request.httpMethod,
                request.requestEntity,
                String::class.java
            )
            return when (responseEntity.statusCode) {
                HttpStatus.OK -> {
                    return when {
                        responseEntity.body.isNullOrBlank() -> null
                        request.responseType == String::class.java -> responseEntity.body as T?
                        request.responseType is Class<*> -> {
                            val body = responseEntity.body
                            if (body.isNullOrBlank())
                                null
                            else
                                json.fromJson(body, request.responseType as Class<T>)
                        }
                        else -> {
                            val body = responseEntity.body
                            if (body.isNullOrBlank())
                                null
                            else
                                json.fromJson(body, request.responseType)
                        }
                    }

                }
                HttpStatus.NO_CONTENT -> null
                else -> {
                    val exception = json.fromJson(responseEntity.body!!, exceptionClass)
                    handler(responseEntity.statusCode, exception)
                    throw RuntimeException("you must throw exception in handler")
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun <T> doRequestForEntity(request: Request): ResponseEntity<T>? = restTemplate.execute(
        request.uri,
        request.httpMethod,
        HttpEntityRequestCallback(request.requestEntity, request.responseType),
        ResponseEntityResponseExtractor(request.responseType)
    )

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