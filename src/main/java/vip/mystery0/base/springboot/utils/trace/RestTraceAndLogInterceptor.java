package vip.mystery0.base.springboot.utils.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.mystery0.base.springboot.config.BaseProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mystery0
 * @date 2019/11/29
 */
public class RestTraceAndLogInterceptor implements HandlerInterceptor {

    @Autowired
    private BaseProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TraceHelper.beginTrace(request);
        TraceLogUtil.logRequest(request, properties.getLogMaxLength());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TraceLogUtil.logResponse(null);
    }
}
