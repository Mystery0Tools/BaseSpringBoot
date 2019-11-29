package vip.mystery0.base.springboot.constant;

/**
 * @author mystery0
 * @date 2019/11/29
 */
public interface Constants {
    /**
     * 追踪id
     */
    String MDC_TRACE_ID = "trace_id";
    /**
     * 请求开始时间
     */
    String MDC_START_TIME = "mdc_startTime";
    /**
     * 客户端ip
     */
    String MDC_IP = "mdc_ip";
    /**
     * 请求uri
     */
    String MDC_URI = "mdc_uri";
}
