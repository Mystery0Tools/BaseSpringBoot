package vip.mystery0.base.springboot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vip.mystery0.base.springboot.utils.SnowflakeIdWorker

/**
 * @author mystery0
 */
@Configuration
class BaseAutoConfiguration {
    @Bean
    fun snowflakeIdWorker(): SnowflakeIdWorker {
        return SnowflakeIdWorker()
    }
}