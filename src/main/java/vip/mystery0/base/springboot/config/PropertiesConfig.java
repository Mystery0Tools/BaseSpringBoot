package vip.mystery0.base.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfig {

    @Value("${mystery0.request.log.max-length}")
    private int logMaxLength;

    public int getLogMaxLength() {
        return logMaxLength;
    }

    @Value("${mystery0.web.log.point}")
    private String webLogPoint;

    public String getWebLogPoint() {
        return webLogPoint;
    }

    @Value("${mystery0.exception.log.point}")
    private String exceptionLogPoint;

    public String getExceptionLogPoint() {
        return exceptionLogPoint;
    }

    @Value("${mystery0.redis.key-prefix}")
    private String redisPrefix;

    public String getRedisPrefix() {
        return redisPrefix;
    }
}
