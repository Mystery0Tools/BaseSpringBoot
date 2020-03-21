package vip.mystery0.base.springboot.utils

import org.slf4j.Logger

/**
 * @author mystery0
 * Create at 2020/3/21
 */
fun Logger.withError(message: String = "", block: () -> Unit): Unit = try {
    block()
} catch (e: Exception) {
    error(message, e)
}

fun <R> Logger.withError(message: String = "", block: () -> R?): R? = try {
    block()
} catch (e: Exception) {
    error(message, e)
    null
}