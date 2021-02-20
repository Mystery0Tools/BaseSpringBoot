package vip.mystery0.base.springboot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vip.mystery0.base.springboot.utils.SnowflakeIdWorker

/**
 * @author mystery0
 */
@Configuration
open class BaseAutoConfiguration {
    @Bean
    open fun snowflakeIdWorker(): SnowflakeIdWorker = SnowflakeIdWorker()
}