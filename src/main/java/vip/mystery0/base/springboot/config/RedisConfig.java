package vip.mystery0.base.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import vip.mystery0.base.springboot.service.RedisService;

@Configuration
public class RedisConfig {
    @Bean
    public RedisService redisService() {
        return new RedisService();
    }
}
