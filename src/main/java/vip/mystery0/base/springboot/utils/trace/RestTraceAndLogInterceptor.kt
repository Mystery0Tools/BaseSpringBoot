package vip.mystery0.base.springboot.utils.trace

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.utils.trace.TraceHelper.beginTrace
import vip.mystery0.base.springboot.utils.trace.TraceHelper.endTrace
import vip.mystery0.base.springboot.utils.trace.TraceLogUtil.logRequest
import vip.mystery0.base.springboot.utils.trace.TraceLogUtil.logResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author mystery0
 */
class RestTraceAndLogInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var properties: BaseProperties

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        beginTrace(request)
        logRequest(request, properties.logMaxLength)
        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        logResponse(null)
        endTrace()
    }
}