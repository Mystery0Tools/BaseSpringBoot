package vip.mystery0.base.springboot.utils.rest

import com.fasterxml.jackson.core.type.TypeReference

/**
 * @author Mystery0
 */
interface JSON {
    fun <T> fromJson(json: String, clazz: Class<T>): T
    fun <T> fromJson(json: String, typeReference: TypeReference<T>): T
    fun <T> toJson(obj: T): String
}