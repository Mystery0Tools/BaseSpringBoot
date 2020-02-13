package vip.mystery0.base.springboot.utils.trace

import org.slf4j.MDC
import vip.mystery0.base.springboot.constant.*
import vip.mystery0.tools.kotlin.utils.getClientIP
import vip.mystery0.tools.kotlin.utils.host
import java.util.*
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

/**
 * @author mystery0
 */
object TraceHelper {
    /**
     * 开始追踪。
     *
     * 用于生成追踪信息，供拦截器、执行任务的线程调用。
     *
     * @param request Servlet 请求信息
     */
    fun beginTrace(request: HttpServletRequest) {
        val currentTime = System.currentTimeMillis()
        val traceId = generateTraceId()
        MDC.put(MDC_TRACE_ID, traceId)
        MDC.put(MDC_URI, request.method + " " + request.requestURI)
        MDC.put(MDC_START_TIME, currentTime.toString())
        MDC.put(MDC_IP, request.getClientIP())
        var language = request.getHeader(HEADER_LANGUAGE)
        if (language.isNullOrBlank())
            language = LANGUAGE_DEFAULT
        MDC.put(MDC_LANGUAGE, language)
        MDC.put(MDC_REQUEST_HOST, request.host)
    }

    /**
     * 结束追踪。
     *
     * 用于执行任务的线程，在任务逻辑执行完毕，交还给线程池之前，调用它来清理日志数据。
     */
    fun endTrace() {
        MDC.remove(MDC_TRACE_ID)
        MDC.remove(MDC_URI)
        MDC.remove(MDC_START_TIME)
        MDC.remove(MDC_IP)
        MDC.remove(MDC_LANGUAGE)
        MDC.remove(MDC_REQUEST_HOST)
    }

    /**
     * 获取23位的追踪Id
     */
    private fun generateTraceId(): String {
        var uuid = UUID.randomUUID().toString()
        uuid = uuid.replace("-".toRegex(), "")
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(uuid)
        var uuidNumber = m.replaceAll("").trim { it <= ' ' }
        uuidNumber = uuidNumber.replace(" ".toRegex(), "")
        val timeStr = System.currentTimeMillis().toString()
        if (uuidNumber.length > 10)
            uuidNumber = uuidNumber.substring(uuidNumber.length - 10)
        return timeStr + uuidNumber
    }

    fun getClientIP(request: HttpServletRequest): String {
        val ip = MDC.get(MDC_IP)
        return if (ip.isNullOrBlank()) request.getClientIP() ?: "" else ip
    }
}