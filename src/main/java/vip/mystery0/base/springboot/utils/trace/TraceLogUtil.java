package vip.mystery0.base.springboot.utils.trace;

import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import vip.mystery0.base.springboot.constant.Constants;
import vip.mystery0.tools.java.factory.JsonFactory;
import vip.mystery0.tools.java.utils.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mystery0
 */
public class TraceLogUtil {
    private static final Logger log = LoggerFactory.getLogger(TraceLogUtil.class);

    /**
     * 记录请求
     *
     * @param request   请求体
     * @param maxLength 参数打印最大长度
     */
    public static void logRequest(HttpServletRequest request,
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
        log.info("║ {}", TimeUtil.formatDateTime(LocalDateTime.now()));
        log.info("║ {}", MDC.get(Constants.MDC_URI));
        if (!"".equals(args)) {
            log.info("║ params: 【{}】", params);
        }
        log.info("║ IP: {}", TraceHelper.getClientIP(request));
        log.info("╙──────────────────────");
    }

    public static void logResponse(Object result) {
        log.info("╓──────────────────────");
        Long costTime = getCostTime();
        if (costTime != null) {
            log.info("║ request cost time: {}ms", costTime);
        }
        log.info("║ {}", MDC.get(Constants.MDC_URI));
        if (result != null) {
            String json = JsonFactory.toJson(result);
            log.info("║ return: {}", json);
        }
        log.info("╚══════════════════════");
    }

    private static Long getCostTime() {
        try {
            return (System.currentTimeMillis() - Long.parseLong(MDC.get(Constants.MDC_START_TIME)));
        } catch (Exception e) {
            log.warn("get cost time failed");
            return null;
        }
    }
}
