package vip.mystery0.base.springboot

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.utils.SnowflakeIdWorker
import vip.mystery0.base.springboot.utils.rest.fuse.FuseService
import vip.mystery0.base.springboot.utils.rest.handler.RestResponseErrorHandler
import vip.mystery0.base.springboot.utils.rest.interceptor.LoggingClientHttpRequestInterceptor

/**
 * @author mystery0
 */
@Configuration
@EnableConfigurationProperties(BaseProperties::class)
class BaseAutoConfiguration {
    @Bean
    fun snowflakeIdWorker(): SnowflakeIdWorker = SnowflakeIdWorker()

    @Bean
    fun fuseService(): FuseService = FuseService()

    @Bean
    fun restTemplate(fuseService: FuseService, baseProperties: BaseProperties): RestTemplate {
        val restTemplate = RestTemplate()
        //添加日志拦截器
        restTemplate.requestFactory = BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory())
        restTemplate.interceptors.add(LoggingClientHttpRequestInterceptor())
        restTemplate.errorHandler = RestResponseErrorHandler(fuseService, baseProperties)
        return restTemplate
    }
}