package vip.mystery0.base.springboot.config

import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import vip.mystery0.base.springboot.utils.trace.TraceHelper
import vip.mystery0.base.springboot.utils.trace.TraceLogUtil
import java.util.*

object WebLogAspect {
    fun log(joinPoint: ProceedingJoinPoint): Any? = doWebLog(joinPoint)
}

fun doWebLog(joinPoint: ProceedingJoinPoint): Any? {
    val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
    val request = Objects.requireNonNull(attributes)!!.request
    TraceHelper.beginTrace(request)
    TraceLogUtil.logRequest(request)
    val result = joinPoint.proceed()
    TraceLogUtil.logResponse(result)
    TraceHelper.endTrace()
    return result
}