package vip.mystery0.base.springboot.utils.trace;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import vip.mystery0.base.springboot.constant.Constants;
import vip.mystery0.tools.java.utils.IPUtil;
import vip.mystery0.tools.kotlin.utils.IPUtilKt;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mystery0
 */
public class TraceHelper {

    /**
     * 开始追踪。
     * <p>用于生成追踪信息，供拦截器、执行任务的线程调用。</p>
     *
     * @param request Servlet 请求信息
     */
    public static void beginTrace(HttpServletRequest request) {
        long curTime = System.currentTimeMillis();
        String traceId = generateTraceId();
        MDC.put(Constants.MDC_TRACE_ID, traceId);
        MDC.put(Constants.MDC_URI, request.getMethod() + " " + request.getRequestURI());
        MDC.put(Constants.MDC_START_TIME, String.valueOf(curTime));
        MDC.put(Constants.MDC_IP, getClientIP(request));
        String language = request.getHeader(Constants.HEADER_LANGUAGE);
        if (StringUtils.isEmpty(language)) {
            language = Constants.LANGUAGE_DEFAULT;
        }
        MDC.put(Constants.MDC_LANGUAGE, language);
        MDC.put(Constants.MDC_REQUEST_HOST, IPUtilKt.getHost(request));
    }

    /**
     * 结束追踪。
     * <p>用于执行任务的线程，在任务逻辑执行完毕，交还给线程池之前，调用它来清理日志数据。</p>
     */
    public static void endTrace() {
        MDC.remove(Constants.MDC_TRACE_ID);
        MDC.remove(Constants.MDC_URI);
        MDC.remove(Constants.MDC_START_TIME);
        MDC.remove(Constants.MDC_IP);
        MDC.remove(Constants.MDC_LANGUAGE);
        MDC.remove(Constants.MDC_REQUEST_HOST);
    }

    /**
     * 获取23位的追踪Id
     */
    private static String generateTraceId() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(uuid);
        String uuidNumber = m.replaceAll("").trim();
        uuidNumber = uuidNumber.replaceAll(" ", "");
        String timeStr = String.valueOf(System.currentTimeMillis());

        if (uuidNumber.length() > 10) {
            uuidNumber = uuidNumber.substring(uuidNumber.length() - 10);
        }
        return timeStr + uuidNumber;
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = MDC.get(Constants.MDC_IP);
        if (StringUtils.isEmpty(ip)) {
            return IPUtil.getClientIP(request);
        } else {
            return ip;
        }
    }
}
