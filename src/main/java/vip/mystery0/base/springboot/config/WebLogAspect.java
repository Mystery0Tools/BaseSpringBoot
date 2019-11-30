package vip.mystery0.base.springboot.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.mystery0.base.springboot.utils.trace.TraceHelper;
import vip.mystery0.base.springboot.utils.trace.TraceLogUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class WebLogAspect {
    public static void doWebLog(ProceedingJoinPoint joinPoint, int maxLength) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        TraceHelper.beginTrace(request);
        TraceLogUtil.logRequest(request, maxLength);
        Object result = joinPoint.proceed();
        TraceLogUtil.logResponse(result);
        TraceHelper.endTrace();
    }
}
