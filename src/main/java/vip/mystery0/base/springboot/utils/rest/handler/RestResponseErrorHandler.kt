package vip.mystery0.base.springboot.utils.rest.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.utils.rest.fuse.FuseService
import java.io.IOException
import java.net.URI

/**
 * @author mystery0
 */
@Component
class RestResponseErrorHandler : ResponseErrorHandler {
    private val logger = LoggerFactory.getLogger(RestResponseErrorHandler::class.java)

    @Autowired
    private lateinit var fuseService: FuseService
    @Autowired
    private lateinit var baseProperties: BaseProperties

    @Throws(IOException::class)
    override fun hasError(response: ClientHttpResponse): Boolean {
        if (baseProperties.enableFuse) {
            return true
        } else {
            if (response.statusCode != HttpStatus.OK) {
                logger.warn("Status code: {} , text {}", response.statusCode, response.statusText)
                logger.warn("Response: {}", response.body)
                return true
            }
            return false
        }
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