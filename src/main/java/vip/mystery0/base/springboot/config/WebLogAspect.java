package vip.mystery0.base.springboot.config;

import org.apache.tomcat.util.buf.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.mystery0.tools.java.factory.JsonFactory;
import vip.mystery0.tools.java.utils.IPUtil;
import vip.mystery0.tools.java.utils.TimeUtil;
import vip.mystery0.tools.kotlin.model.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Pointcut("execution(public * PropertiesConfig.getWebLogPoint())")
    private void webLog() {
    }

    @Pointcut("execution(public * PropertiesConfig.getExceptionLogPoint())")
    private void handleError() {
    }

    @Before("webLog()")
    private void doBeforeWeb(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        List<String> params = new ArrayList<>();
        request.getParameterMap().forEach((s, strings) -> {
            String value = StringUtils.join(strings);
            if (value.length() > propertiesConfig.getLogMaxLength()) {
                String start = value.substring(0, 4);
                String end = value.substring(value.length() - 4);
                params.add(s + "=>" + start + "...." + end);
            } else {
                params.add(s + "=>" + value);
            }
        });
        String args = StringUtils.join(params);

        log.info("╔══════════════════════");
        log.info("║ " + TimeUtil.toDateTimeString(Calendar.getInstance()));
        log.info("║ " + request.getMethod() + " " + request.getRequestURI());
        if (!"".equals(args)) {
            log.info("║ params: 【" + params + "】");
        }
        log.info("║ IP: " + IPUtil.getClientIP(request));
        log.info("╙──────────────────────");
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    private void doAfterWebReturning(Object ret) {
        log.info("╓──────────────────────");
        log.info("║ return: " + JsonFactory.toJson(ret));
        log.info("╚══════════════════════");
    }

    @AfterReturning(returning = "ret", pointcut = "handleError()")
    private void doAfterErrorReturning(Response<Object> ret) {
        log.info("╓──────────────────────");
        log.info("║ return: " + JsonFactory.toJson(ret));
        log.info("╚══════════════════════");
    }
}
