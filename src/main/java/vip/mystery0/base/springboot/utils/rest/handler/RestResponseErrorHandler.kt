package vip.mystery0.base.springboot.utils.rest.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.utils.rest.fuse.FuseService
import java.io.IOException
import java.net.URI

/**
 * @author mystery0
 */
class RestResponseErrorHandler(
    private val fuseService: FuseService,
    private val baseProperties: BaseProperties
) : ResponseErrorHandler {
    private val logger = LoggerFactory.getLogger(RestResponseErrorHandler::class.java)

    @Throws(IOException::class)
    override fun hasError(response: ClientHttpResponse): Boolean {
        return if (baseProperties.enableFuse) true
        else response.statusCode != HttpStatus.OK
    }

    override fun handleError(response: ClientHttpResponse) {}

    override fun handleError(url: URI, method: HttpMethod, response: ClientHttpResponse) {
        super.handleError(url, method, response)
        val statusCode = response.statusCode
        if (fuseService.isRequestFailed(statusCode)) {
            logger.warn("Status code: {} , text {}", response.statusCode, response.statusText)
            logger.warn("Response: {}", response.body)
            fuseService.logFailed(url, statusCode)
        } else {
            fuseService.logSuccess(url)
        }
    }
}