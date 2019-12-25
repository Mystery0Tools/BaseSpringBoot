package vip.mystery0.base.springboot.utils.rest.interceptor

import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * @author mystery0
 */
class LoggingClientHttpRequestInterceptor : ClientHttpRequestInterceptor {
    private val logger = LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor::class.java)

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        traceRequest(request, body)
        return execution.execute(request, body)
    }

    private fun traceRequest(request: HttpRequest, body: ByteArray) {
        logger.debug("===========================request begin================================================")
        logger.debug("URI         : {}", request.uri)
        logger.debug("Method      : {}", request.method)
        logger.debug("Headers     : {}", request.headers)
        logger.debug("Request body: {}", String(body, StandardCharsets.UTF_8))
        logger.debug("==========================request end===================================================")
    }
}