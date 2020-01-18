package vip.mystery0.base.springboot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author mystery0
 */
@ConfigurationProperties(prefix = "base-config")
class BaseProperties {
    var logMaxLength = 0
    lateinit var redisPrefix: String
    var enableFuse = false
}