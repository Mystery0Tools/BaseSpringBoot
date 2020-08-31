package vip.mystery0.base.springboot.utils.rest.interceptor

import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import vip.mystery0.tools.java.utils.IOUtils
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * @author mystery0
 */
class LoggingClientHttpRequestInterceptor : ClientHttpRequestInterceptor {
    private val logger = LoggerFactory.getLogger("LoggingClientHttpRequestInterceptor")

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        traceRequest(request, body)
        val now = System.currentTimeMillis()
        val response = execution.execute(request, body)
        traceResponse(response, now)
        return response
    }

    private fun traceRequest(request: HttpRequest, body: ByteArray) {
        logger.debug("===========================request begin================================================")
        logger.debug("URI         : {} {}", request.method, request.uri)
        logger.debug("Headers     : {}", request.headers)
        if (body.size < 102400) {
            logger.debug("Request body: {}", String(body, StandardCharsets.UTF_8))
        }
        logger.debug("==========================request end================================================")
    }

    @Throws(IOException::class)
    private fun traceResponse(response: ClientHttpResponse, now: Long) {
        val body = if (response.headers.contentLength < 102400) {
            String(IOUtils.toByteArray(response.body), StandardCharsets.UTF_8)
        } else {
            "[[response body too big]]"
        }
        logger.debug("============================response begin==========================================")
        logger.debug("Status code  : {} {}", response.statusCode, response.statusText)
        logger.debug("Headers      : {}", response.headers)
        logger.debug("Response body: {}", body)
        logger.debug("Cost time    : {}ms", System.currentTimeMillis() - now)
        logger.debug("=======================response end=================================================")
    }
}