package vip.mystery0.base.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mystery0
 */
@ConfigurationProperties(prefix = "base-config")
public class BaseProperties {
    private int logMaxLength;
    private String redisPrefix;

    public int getLogMaxLength() {
        return logMaxLength;
    }

    public void setLogMaxLength(int logMaxLength) {
        this.logMaxLength = logMaxLength;
    }

    public String getRedisPrefix() {
        return redisPrefix;
    }

    public void setRedisPrefix(String redisPrefix) {
        this.redisPrefix = redisPrefix;
    }

    @Override
    public String toString() {
        return "BaseProperties{" +
                "logMaxLength=" + logMaxLength +
                ", redisPrefix='" + redisPrefix + '\'' +
                '}';
    }
}
