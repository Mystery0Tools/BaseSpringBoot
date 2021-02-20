package vip.mystery0.base.springboot.config.log

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.zalando.logbook.HttpLogFormatter
import org.zalando.logbook.HttpLogWriter
import org.zalando.logbook.HttpRequest
import org.zalando.logbook.HttpResponse

/**
 * @author mystery0
 * Create at 2021/1/19
 */
@Configuration
open class LogbookConfiguration {
    @Bean
    @Primary
    @ConditionalOnMissingBean(HttpLogWriter::class)
    open fun logWriter(): HttpLogWriter = LogbookWriter()

    @Bean
    @Primary
    @ConditionalOnMissingBean(HttpLogFormatter::class)
    open fun logFormatter(): HttpLogFormatter = LogbookFormatter()

    @Bean
    @ConditionalOnMissingBean(LogCondition::class)
    open fun logCondition(): LogCondition = object : LogCondition {
        override fun logRequest(request: HttpRequest): Boolean = true

        override fun logResponse(response: HttpResponse): Boolean = true
    }
}