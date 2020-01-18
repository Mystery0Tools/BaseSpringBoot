package vip.mystery0.base.springboot.filter

import vip.mystery0.base.springboot.utils.trace.TraceLogUtil
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * @author mystery0
 * Create at 2020/1/18
 */
class RequestLoggingFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse?, chain: FilterChain?) {
        val body = String(request.inputStream.readBytes())
        TraceLogUtil.logRequestBody(body)
        chain?.doFilter(request, response)
    }
}