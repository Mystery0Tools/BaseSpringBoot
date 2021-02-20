package vip.mystery0.base.springboot.utils

import org.slf4j.MDC
import org.zalando.logbook.HttpRequest
import vip.mystery0.base.springboot.constant.MDC_IP
import vip.mystery0.base.springboot.constant.MDC_TRACE_ID
import vip.mystery0.base.springboot.constant.MDC_URI
import vip.mystery0.base.springboot.utils.TraceUtil.currentTraceId

/**
 * 全链路追踪、日志、应答包中的 logId 生成的辅助类。
 *
 * @author mystery0
 * @date 2019/04/14
 */
object TraceHelper {
    /**
     * 开始追踪。
     *
     * 用于生成追踪信息，供拦截器、执行任务的线程调用。
     *
     * @param request Servlet 请求信息
     */
    fun beginTrace(request: HttpRequest?) {
        var uri: String? = ""
        var ip: String? = ""
        if (request != null) {
            uri = request.requestUri
            ip = getRemoteHost(request.remote)
        }
        currentTraceId
        MDC.put(MDC_URI, uri)
        MDC.put(MDC_IP, ip)
    }

    /**
     * 结束追踪。
     *
     * 用于执行任务的线程，在任务逻辑执行完毕，交还给线程池之前，调用它来清理日志数据。
     */
    fun endTrace() {
        MDC.remove(MDC_TRACE_ID)
        MDC.remove(MDC_IP)
        MDC.remove(MDC_URI)
    }

    private fun getRemoteHost(remote: String?): String? {
        if (remote == null) {
            return null
        }
        return if ("0:0:0:0:0:0:0:1" == remote) "127.0.0.1" else remote
    }
}