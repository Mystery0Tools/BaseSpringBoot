package vip.mystery0.base.springboot.config.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * @author mystery0
 * 拦截打印对外请求日志
 */
class LoggingClientHttpRequestInterceptor : ClientHttpRequestInterceptor {
    private fun isFileRequest(httpHeaders: HttpHeaders?): Boolean {
        if (httpHeaders == null) {
            return false
        }
        val mediaType = httpHeaders.contentType
        return mediaType?.includes(MediaType.MULTIPART_FORM_DATA) ?: false
    }

    private fun isFileResponse(httpHeaders: HttpHeaders?): Boolean {
        if (httpHeaders == null) {
            return false
        }
        val mediaType = httpHeaders.contentType
        return if (mediaType != null) {
            !mediaType.includes(MediaType.APPLICATION_JSON)
        } else {
            false
        }
    }

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        traceRequest(request, body)
        val response = execution.execute(request, body)
        traceResponse(response)
        return response
    }

    private fun traceRequest(
        request: HttpRequest,
        body: ByteArray
    ) {
        val bodyString: String = when {
            isFileRequest(request.headers) -> "[[multipart form data]]"
            body.size < MAX_BODY_SIZE -> String(body, StandardCharsets.UTF_8)
            else -> "[[request body too big]]"
        }
        log.debug("======================= request begin ===============================================")
        log.debug("URI         : {} {}", request.method, request.uri)
        log.debug("Headers     : {}", request.headers)
        log.debug("Request body: {}", bodyString)
        log.debug("======================= request end =================================================")
    }

    @Throws(IOException::class)
    private fun traceResponse(response: ClientHttpResponse) {
        val bodyString: String = when {
            isFileResponse(response.headers) -> "[[not json body]]"
            response.headers.contentLength < MAX_BODY_SIZE -> String(response.body.readAllBytes())
            else -> "[[request body too big]]"
        }
        log.debug("============================response begin==========================================")
        log.debug("Status code  : {} {}", response.statusCode, response.statusText)
        log.debug("Headers      : {}", response.headers)
        log.debug("Response body: {}", bodyString)
        log.debug("=======================response end=================================================")
    }

    companion object {
        private val log = LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor::class.java)
        private const val MAX_BODY_SIZE = 102400L
    }
}