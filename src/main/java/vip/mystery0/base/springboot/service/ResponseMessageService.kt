package vip.mystery0.base.springboot.service

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import vip.mystery0.base.springboot.constant.LANGUAGE_DEFAULT
import vip.mystery0.base.springboot.constant.MDC_LANGUAGE
import vip.mystery0.tools.kotlin.factory.toJson
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

/**
 * @author mystery0
 * Create at 2019/12/7
 */
@Service
class ResponseMessageService {
    private val log = LoggerFactory.getLogger(ResponseMessageService::class.java)

    private val map = ConcurrentHashMap<String, ResourceBundle>()

    fun registerLocaleTranslate(locale: String, resourceBundle: ResourceBundle) {
        map[locale] = resourceBundle
    }

    private fun getResourceBundle(locale: String?): ResourceBundle? {
        if (locale == null) return null
        return map[locale]
    }

    fun getTranslate(name: String): String {
        val defaultValue = getTranslate(name, LANGUAGE_DEFAULT, "default message")
        return getTranslate(name, MDC.get(MDC_LANGUAGE), defaultValue)
    }

    fun getTranslate(name: String, defaultValue: String): String {
        return getTranslate(name, MDC.get(MDC_LANGUAGE), defaultValue)
    }

    fun getTranslate(name: String, locale: String?, defaultValue: String): String {
        val resourceBundle = getResourceBundle(locale) ?: return defaultValue
        val result = resourceBundle.getString(name)
        return if (StringUtils.isEmpty(result)) {
            defaultValue
        } else result
    }

    fun fillTemplate(template: String, vararg params: Any): String {
        var result = template
        for (param in params) {
            result = BRACKET.matcher(result).replaceFirst(param.toJson())
        }
        return result
    }

    companion object {
        private val BRACKET = Pattern.compile("\\{\\w*}")
    }
}