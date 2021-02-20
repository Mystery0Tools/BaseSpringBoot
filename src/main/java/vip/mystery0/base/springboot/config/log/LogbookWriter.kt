package vip.mystery0.base.springboot.config.log

import org.zalando.logbook.Correlation
import org.zalando.logbook.HttpLogWriter
import org.zalando.logbook.Precorrelation

/**
 * @author mystery0
 * Create at 2021/1/19
 */
class LogbookWriter : HttpLogWriter {
    override fun isActive(): Boolean = true

    override fun write(precorrelation: Precorrelation, request: String) {
        //Do Nothing
    }

    override fun write(correlation: Correlation, response: String) {
        //Do Nothing
    }
}