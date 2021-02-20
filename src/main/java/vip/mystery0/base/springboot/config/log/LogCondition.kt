package vip.mystery0.base.springboot.config.log

import org.zalando.logbook.HttpRequest
import org.zalando.logbook.HttpResponse

/**
 * @author mystery0
 * Create at 2021/2/20
 */
interface LogCondition {
    fun logRequest(request: HttpRequest): Boolean

    fun logResponse(response: HttpResponse): Boolean
}