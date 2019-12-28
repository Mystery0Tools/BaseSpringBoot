package vip.mystery0.base.springboot.config

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @author mystery0
 */
@Component
class BaseApplicationContext : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {
        private var context: ApplicationContext? = null

        fun <T> getBean(requiredType: Class<T>): T = context!!.getBean(requiredType)
    }
}