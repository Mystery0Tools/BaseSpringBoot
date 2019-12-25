package vip.mystery0.base.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vip.mystery0.base.springboot.constant.Constants;
import vip.mystery0.tools.java.factory.JsonFactory;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author mystery0
 * Create at 2019/12/7
 */
@Service
public class ResponseMessageService {
    private static final Logger log = LoggerFactory.getLogger(ResponseMessageService.class);
    private static final Pattern BRACKET = Pattern.compile("\\{\\w*}");

    private ConcurrentHashMap<String, ResourceBundle> map = new ConcurrentHashMap<>();

    public void registerLocaleTranslate(String locale, ResourceBundle resourceBundle) {
        map.put(locale, resourceBundle);
    }

    private ResourceBundle getResourceBundle(String locale) {
        return map.get(locale);
    }

    public String getTranslate(String name) {
        String defaultValue = getTranslate(name, Constants.LANGUAGE_DEFAULT, "default message");
        return getTranslate(name, defaultValue);
    }

    public String getTranslate(String name, String defaultValue) {
        return getTranslate(name, MDC.get(Constants.MDC_LANGUAGE), defaultValue);
    }

    public String getTranslate(String name, String locale, String defaultValue) {
        ResourceBundle resourceBundle = getResourceBundle(locale);
        if (resourceBundle == null) {
            return defaultValue;
        }
        String result = resourceBundle.getString(name);
        if (StringUtils.isEmpty(result)) {
            return defaultValue;
        }
        return result;
    }

    public String fillTemplate(String template, Object... params) {
        String result = template;
        for (Object param : params) {
            result = BRACKET.matcher(result).replaceFirst(JsonFactory.toJson(param));
        }
        return result;
    }
}
