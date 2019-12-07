package vip.mystery0.base.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
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
    private static final Pattern BRACKET = Pattern.compile("{}");

    private ConcurrentHashMap<Locale, ResourceBundle> map = new ConcurrentHashMap<>();

    public void registerLocaleTranslate(Locale locale, ResourceBundle resourceBundle) {
        map.put(locale, resourceBundle);
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        return map.get(locale);
    }

    public String getTranslate(String name, Locale locale, String defaultValue) {
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
            result = BRACKET.matcher(result).replaceFirst(param.toString());
        }
        return result;
    }
}
