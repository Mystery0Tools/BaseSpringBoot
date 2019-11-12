package vip.mystery0.base.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasePropertiesConfig {

    @Value("${mystery0.request.log.max-length}")
    private int logMaxLength;

    public int getLogMaxLength() {
        return logMaxLength;
    }

    @Value("${mystery0.redis.key-prefix}")
    private String redisPrefix;

    public String getRedisPrefix() {
        return redisPrefix;
    }
}
