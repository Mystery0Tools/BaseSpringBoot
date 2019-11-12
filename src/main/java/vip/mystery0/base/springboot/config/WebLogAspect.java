package vip.mystery0.base.springboot.config;

import org.apache.tomcat.util.buf.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.mystery0.tools.java.factory.JsonFactory;
import vip.mystery0.tools.java.utils.IPUtil;
import vip.mystery0.tools.java.utils.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    public static void doWebLog(ProceedingJoinPoint joinPoint, int maxLength) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        logRequest(request, log, maxLength);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        log.info("╓──────────────────────");
        log.info("║ request cost time: " + (System.currentTimeMillis() - start) + " ms");
        String json = JsonFactory.toJson(result);
        if (json != null) {
            log.info("║ return: " + json);
        }
        log.info("╚══════════════════════");
    }

    public static void logRequest(HttpServletRequest request,
                                  Logger log,
                                  int maxLength) {
        List<String> params = new ArrayList<>();
        request.getParameterMap().forEach((s, strings) -> {
            String value = StringUtils.join(strings);
            if (value.length() > maxLength) {
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
}
