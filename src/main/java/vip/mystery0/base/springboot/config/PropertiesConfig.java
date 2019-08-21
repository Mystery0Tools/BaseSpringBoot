package vip.mystery0.base.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mystery0
 * @date 2019-08-13
 */
@Component
public class PropertiesConfig {

    @Value("${mystery0.request.log.max-length}")
    private int logMaxLength;

    public int getLogMaxLength() {
        return logMaxLength;
    }

    @Value("${mystery0.web.log.point}")
    private int webLogPoint;

    public int getWebLogPoint() {
        return webLogPoint;
    }
}
