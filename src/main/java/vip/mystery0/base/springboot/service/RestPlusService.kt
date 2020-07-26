package vip.mystery0.base.springboot.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.model.ServiceApiException
import vip.mystery0.base.springboot.utils.rest.JSON
import vip.mystery0.base.springboot.utils.rest.RestTemplatePlus
import vip.mystery0.base.springboot.utils.rest.fuse.FuseService
import vip.mystery0.tools.kotlin.factory.fromJson
import vip.mystery0.tools.kotlin.factory.toJson
import vip.mystery0.tools.kotlin.model.Response
import java.lang.reflect.Type

/**
 * @author mystery0
 * Create at 2019/12/25
 */
@Service
class RestPlusService(
    restTemplate: RestTemplate
) {
    private val restTemplatePlus: RestTemplatePlus<Response<*>> = RestTemplatePlus(
        Response::class.java, { _, exception -> throw ServiceApiException(exception) },
        object : JSON {
            override fun <T> fromJson(json: String, clazz: Class<T>): T = json.fromJson(clazz)
            override fun <T> fromJson(json: String, type: Type): T = json.fromJson(type)
            override fun <T> toJson(obj: T): String = obj.toJson()
        },
        restTemplate
    )

    fun <T> get(
        url: String,
        type: Type = Void::class.java
    ): T? = restTemplatePlus.get(url, type)

    fun <T> getForEntity(
        url: String,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = restTemplatePlus.getForEntity(url, type)

    fun <T> post(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): T? = restTemplatePlus.post(url, request, type)

    fun <T> postForEntity(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = restTemplatePlus.postForEntity(url, request, type)

    fun <T> put(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): T? = restTemplatePlus.put(url, request, type)

    fun <T> putForEntity(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = restTemplatePlus.putForEntity(url, request, type)

    fun <T> delete(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): T? = restTemplatePlus.delete(url, request, type)

    fun <T> deleteForEntity(
        url: String,
        request: Any? = null,
        type: Type = Void::class.java
    ): ResponseEntity<T>? = restTemplatePlus.deleteForEntity(url, request, type)
}