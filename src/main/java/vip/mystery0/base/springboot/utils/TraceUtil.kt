package vip.mystery0.base.springboot.utils

import org.slf4j.MDC
import vip.mystery0.base.springboot.constant.MDC_TRACE_ID
import java.util.*
import java.util.regex.Pattern

/**
 * @author mystery0
 * Create at 2021/1/14
 */
object TraceUtil {
    @JvmStatic
    val currentTraceId: String
        get() {
            val traceId = MDC.get(MDC_TRACE_ID)
            val id = getTraceId(traceId)
            if (traceId != id) {
                MDC.put(MDC_TRACE_ID, id)
                return id
            }
            return traceId
        }

    fun putTraceId(traceId: String) = MDC.put(MDC_TRACE_ID, traceId)

    fun getTraceId(traceId: String?): String = if (traceId.isNullOrBlank()) generateTraceId() else traceId

    /**
     * 获取23位的Id
     */
    private fun generateTraceId(): String {
        var uuid = UUID.randomUUID().toString()
        uuid = uuid.replace("-", "")
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(uuid)
        var uuidNumber = m.replaceAll("").trim { it <= ' ' }
        uuidNumber = uuidNumber.replace(" ", "")
        val timeStr = System.currentTimeMillis().toString()
        if (uuidNumber.length > 10) {
            uuidNumber = uuidNumber.substring(uuidNumber.length - 10)
        }
        return timeStr + uuidNumber
    }
}