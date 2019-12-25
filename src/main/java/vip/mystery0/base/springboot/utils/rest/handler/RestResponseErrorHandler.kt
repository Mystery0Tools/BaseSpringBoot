package vip.mystery0.base.springboot.utils.rest.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import java.io.IOException

/**
 * @author mystery0
 */
class RestResponseErrorHandler : ResponseErrorHandler {
    private val logger = LoggerFactory.getLogger(RestResponseErrorHandler::class.java)

    @Throws(IOException::class)
    override fun hasError(response: ClientHttpResponse): Boolean {
        if (response.statusCode != HttpStatus.OK) {
            logger.warn("Status code: {} , text {}", response.statusCode, response.statusText)
            logger.warn("Response: {}", response.body)
            return true
        }
        return false
    }

    override fun handleError(response: ClientHttpResponse) {}
}