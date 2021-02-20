package vip.mystery0.base.springboot.config.log

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.zalando.logbook.*
import vip.mystery0.base.springboot.constant.MDC_IP
import vip.mystery0.base.springboot.constant.MDC_URI
import vip.mystery0.base.springboot.utils.TraceHelper.beginTrace
import vip.mystery0.base.springboot.utils.TraceHelper.endTrace
import vip.mystery0.tools.kotlin.factory.toJson

/**
 * @author mystery0
 * Create at 2021/1/19
 */
class LogbookFormatter : HttpLogFormatter {
    @Autowired
    private lateinit var logCondition: LogCondition

    override fun format(precorrelation: Precorrelation, request: HttpRequest): String? {
        beginTrace(request)
        if (logCondition.logRequest(request)) {
            log.trace("++++++++++++++++++-- Request --++++++++++++++++++")
            log.trace("Remote: {}", MDC.get(MDC_IP))
            log.trace("{} {} {}", request.method, MDC.get(MDC_URI), request.protocolVersion)
            writeHeaders(request.headers)
            writeBody(request.bodyAsString)
            log.trace("++++++++++++++++++== Request ==++++++++++++++++++")
        }
        return ""
    }

    override fun format(correlation: Correlation, response: HttpResponse): String? {
        if (logCondition.logResponse(response)) {
            val millis = correlation.duration.toMillis()
            if (millis > 1000) {
                log.warn("++++++++++++++++++-- Response --++++++++++++++++++")
                log.warn("<<Rest API response is slow, please pay attention to optimization>>")
                log.warn("Uri: {}", MDC.get(MDC_URI))
                log.warn("Response: {} {}", response.status, response.reasonPhrase)
                log.warn("Cost: {} ms", correlation.duration.toMillis())
                log.warn("++++++++++++++++++== Response ==++++++++++++++++++")
            } else {
                log.trace("++++++++++++++++++-- Response --++++++++++++++++++")
                log.trace("Uri: {}", MDC.get(MDC_URI))
                log.trace("Response: {} {}", response.status, response.reasonPhrase)
                log.trace("Cost: {} ms", correlation.duration.toMillis())
                log.trace("++++++++++++++++++== Response ==++++++++++++++++++")
            }
        }
        endTrace()
        return ""
    }

    private fun writeHeaders(headers: Map<String, List<String>>) {
        if (headers.isEmpty()) {
            return
        }
        val headersString = headers.toJson()
        log.trace("Headers: {} ", headersString)
    }

    private fun writeBody(body: String) {
        if (body.isBlank()) {
            return
        }
        log.trace("Body: {}", body)
    }

    companion object {
        private val log = LoggerFactory.getLogger("HttpLog")
    }
}