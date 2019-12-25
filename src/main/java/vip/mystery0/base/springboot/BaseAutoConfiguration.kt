package vip.mystery0.base.springboot

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.utils.SnowflakeIdWorker

/**
 * @author mystery0
 */
@Configuration
@EnableConfigurationProperties(BaseProperties::class)
open class BaseAutoConfiguration {
    @Bean
    open fun snowflakeIdWorker(): SnowflakeIdWorker {
        return SnowflakeIdWorker()
    }
}