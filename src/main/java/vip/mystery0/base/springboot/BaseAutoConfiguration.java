package vip.mystery0.base.springboot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.mystery0.base.springboot.config.BaseProperties;
import vip.mystery0.base.springboot.service.RedisService;
import vip.mystery0.base.springboot.service.RestPlusService;
import vip.mystery0.base.springboot.utils.SnowflakeIdWorker;

/**
 * @author mystery0
 * @date 2019/11/16
 */
@Configuration
@EnableConfigurationProperties(BaseProperties.class)
public class BaseAutoConfiguration {
    @Bean
    public RedisService redisService() {
        return new RedisService();
    }

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker();
    }

    @Bean
    public RestPlusService restPlusService() {
        return new RestPlusService();
    }
}
