package vip.mystery0.base.springboot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * @author mystery0
 */
@Service
class BaseProperties {
    @Value("\${base-config.logMaxLength}")
    var logMaxLength = 0

    @Value("\${base-config.redisPrefix}")
    lateinit var redisPrefix: String

    @Value("\${base-config.enableFuse}")
    var enableFuse = false
}