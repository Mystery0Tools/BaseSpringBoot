package vip.mystery0.base.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mystery0
 * Create at 2019/12/7
 */
@Service
public class ResponseMessageService {
    private static final Logger log = LoggerFactory.getLogger(ResponseMessageService.class);

    private static final ConcurrentHashMap<Locale, ResourceBundle> map = new ConcurrentHashMap<>();

    public static void registerLocaleTranslate(Locale locale, ResourceBundle resourceBundle) {
        map.put(locale, resourceBundle);
    }

    private static ResourceBundle getResourceBundle(Locale locale) {
        return map.get(locale);
    }

    public static String getTranslate(String name, Locale locale, String defaultValue) {
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
}
