package vip.mystery0.base.springboot.config.factory

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.util.StringUtils
import vip.mystery0.base.springboot.config.LanguageInterface
import vip.mystery0.base.springboot.constant.MDC_LANGUAGE
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern
import javax.annotation.Nonnull

/**
 * @author mystery0
 * @date 2021/01/15
 */
object ErrorResponseMessageFactory {
    private val log = LoggerFactory.getLogger(ErrorResponseMessageFactory::class.java)
    private val map: MutableMap<String, Map<String, String>> = ConcurrentHashMap()
    private val PATTERN = Pattern.compile("\\{\\w*}")
    private var defaultLanguage: LanguageInterface? = null

    /**
     * 设置默认响应信息语言
     */
    fun setDefaultLanguage(language: LanguageInterface) {
        Preconditions.checkNotNull(language)
        require(map.containsKey(language.languageKey)) { "invalid language, setDefaultLanguage() must be called after registerLocalTranslation()" }
        defaultLanguage = language
    }

    /**
     * 注册响应语言的翻译对应文本
     *
     * @param language    语言枚举
     * @param translation 翻译对应文本
     */
    fun registerLocaleTranslation(language: LanguageInterface, translation: Map<String, String>) {
        Preconditions.checkNotNull(language)
        Preconditions.checkNotNull(translation)
        log.debug("register translation, language: {}", language)
        map[language.languageKey] = translation
    }

    fun fillTemplate(template: String, vararg params: String): String {
        var result = template
        if (!StringUtils.hasText(result)) {
            return result
        }
        for (param in params) {
            result = PATTERN.matcher(result).replaceFirst(param)
        }
        return result
    }

    fun getTranslation(keyName: String): String {
        val languageKey = MDC.get(MDC_LANGUAGE)
        return getTranslation(keyName, languageKey)
    }

    fun getTranslation(keyName: String, languageKey: String?): String {
        return getTranslationOrDefault(keyName, languageKey) {
            val translation = getTranslationOrNull(keyName, defaultLanguage!!.languageKey)
            if (translation == null) {
                log.warn("WARNING!!!")
                log.warn("can't find translation of {}", keyName)
                log.warn("WARNING!!!")
                return@getTranslationOrDefault keyName
            }
            translation
        }
    }

    fun getTranslationOrDefault(keyName: String, defaultValue: String): String {
        val languageKey = MDC.get(MDC_LANGUAGE)
        return getTranslationOrDefault(keyName, languageKey, defaultValue)
    }

    fun getTranslationOrDefault(
        keyName: String,
        languageKey: String?,
        defaultValue: String
    ): String {
        return getTranslationOrDefault(keyName, languageKey) {
            val translation = getTranslationOrNull(keyName, defaultLanguage!!.languageKey)
            translation ?: defaultValue
        }
    }

    private fun getTranslationOrDefault(
        keyName: String,
        languageKey: String?,
        defaultValueSupplier: () -> String
    ): String {
        return getTranslationOrNull(keyName, languageKey)
            ?: return defaultValueSupplier()
    }

    private fun getTranslationOrNull(@Nonnull keyName: String, languageKey: String?): String? {
        val stringMap = getMap(languageKey) ?: return null
        return stringMap[keyName]
    }

    private fun getMap(languageKey: String?): Map<String, String>? {
        return if (languageKey == null) {
            null
        } else map[languageKey]
    }
}