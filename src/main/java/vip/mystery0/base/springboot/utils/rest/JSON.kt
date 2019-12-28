package vip.mystery0.base.springboot.utils.rest

import java.lang.reflect.Type

/**
 * @author Mystery0
 */
interface JSON {
    fun <T> fromJson(json: String, clazz: Class<T>): T
    fun <T> fromJson(json: String, type: Type): T
    fun <T> toJson(obj: T): String
}