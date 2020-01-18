package vip.mystery0.base.springboot.utils.trace

import org.aspectj.lang.ProceedingJoinPoint
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import vip.mystery0.base.springboot.constant.MDC_START_TIME
import vip.mystery0.base.springboot.constant.MDC_URI
import vip.mystery0.tools.kotlin.factory.toJson
import vip.mystery0.tools.kotlin.utils.formatDateTime
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author mystery0
 */
object TraceLogUtil {
    private val log = LoggerFactory.getLogger(TraceLogUtil::class.java)
    /**
     * 记录请求
     *
     * @param request   请求体
     */
    fun logRequest(request: HttpServletRequest, joinPoint: ProceedingJoinPoint) {
        val args = joinPoint.args.toJson()
        log.info("╔══════════════════════")
        log.info("║ {}", LocalDateTime.now().formatDateTime())
        log.info("║ {} {}", request.method, request.requestURI)
        if (args.isNotBlank())
            log.info("║ params: 【{}】", args)
        log.info("║ IP: {}", TraceHelper.getClientIP(request))
        log.info("╙──────────────────────")
    }

    fun logRequestBody(request: HttpServletRequest) {
        val body = String(request.inputStream.readAllBytes())
        log.info("╔══════════════════════")
        log.info("║ {}", LocalDateTime.now().formatDateTime())
        log.info("║ {} {}", request.method, request.requestURI)
        if (body.isNotBlank())
            log.info("║ params: 【{}】", body)
        log.info("║ IP: {}", TraceHelper.getClientIP(request))
        log.info("╙──────────────────────")
    }

    fun logResponse(result: Any?) {
        log.info("╓──────────────────────")
        if (costTime != null)
            log.info("║ request cost time: {}ms", costTime)
        log.info("║ {}", MDC.get(MDC_URI))
        if (result != null)
            log.info("║ return: {}", result.toJson())
        log.info("╚══════════════════════")
    }

    private val costTime: Long?
        get() = try {
            if (MDC.get(MDC_START_TIME) == null) null else System.currentTimeMillis() - MDC.get(MDC_START_TIME).toLong()
        } catch (e: Exception) {
            log.warn("get cost time failed")
            null
        }
}