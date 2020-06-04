package vip.mystery0.base.springboot.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import vip.mystery0.base.springboot.config.BaseProperties
import vip.mystery0.base.springboot.model.ServiceApiException
import vip.mystery0.base.springboot.utils.rest.JSON
import vip.mystery0.base.springboot.utils.rest.RestTemplatePlus
import vip.mystery0.base.springboot.utils.rest.fuse.FuseService
import vip.mystery0.base.springboot.utils.rest.handler.RestResponseErrorHandler
import vip.mystery0.base.springboot.utils.rest.interceptor.LoggingClientHttpRequestInterceptor
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
    private val fuseService: FuseService,
    private val baseProperties: BaseProperties
) {
    private val restTemplatePlus: RestTemplatePlus<Response<*>> = RestTemplatePlus(
        Response::class.java, { throw ServiceApiException(it) },
        object : JSON {
            override fun <T> fromJson(json: String, clazz: Class<T>): T = json.fromJson(clazz)
            override fun <T> fromJson(json: String, type: Type): T = json.fromJson(type)
            override fun <T> toJson(obj: T): String = obj.toJson()
        },
        createRestTemplate()
    )

    fun <T> get(
        url: String,
        type: Type = Void::class.java,
        mapper: ((Response<*>) -> T)? = null,
        vararg uriVariables: Any
    ): T? = restTemplatePlus.get(url, type, mapper, uriVariables)

    fun <T> getForEntity(
        url: String,
        type: Type = Void::class.java,
        vararg uriVariables: Any
    ): ResponseEntity<T>? = restTemplatePlus.getForEntity(url, type, uriVariables)

    fun <T> post(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        mapper: ((Response<*>) -> T)? = null,
        vararg uriVariables: Any
    ): T? = restTemplatePlus.post(url, type, request, mapper, uriVariables)

    fun <T> postForEntity(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        vararg uriVariables: Any
    ): ResponseEntity<T>? = restTemplatePlus.postForEntity(url, type, request, uriVariables)

    fun <T> put(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        mapper: ((Response<*>) -> T)? = null,
        vararg uriVariables: Any
    ): T? = restTemplatePlus.put(url, type, request, mapper, uriVariables)

    fun <T> putForEntity(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        vararg uriVariables: Any
    ): ResponseEntity<T>? = restTemplatePlus.putForEntity(url, type, request, uriVariables)

    fun <T> delete(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        mapper: ((Response<*>) -> T)? = null,
        vararg uriVariables: Any
    ): T? = restTemplatePlus.delete(url, type, request, mapper, uriVariables)

    fun <T> deleteForEntity(
        url: String,
        type: Type = Void::class.java,
        request: Any? = null,
        vararg uriVariables: Any
    ): ResponseEntity<T>? = restTemplatePlus.deleteForEntity(url, type, request, uriVariables)


    private fun createRestTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.interceptors.add(LoggingClientHttpRequestInterceptor())
        restTemplate.errorHandler = RestResponseErrorHandler(fuseService, baseProperties)
        return restTemplate
    }
}