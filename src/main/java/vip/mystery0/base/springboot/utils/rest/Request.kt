package vip.mystery0.base.springboot.utils.rest

import org.slf4j.LoggerFactory
import org.springframework.core.io.AbstractResource
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.io.File
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.net.URI
import java.net.URLEncoder
import java.util.*

/**
 * @author mystery0
 * Create at 2020/6/18
 */
class Request private constructor() {
    lateinit var uri: URI
    lateinit var httpMethod: HttpMethod
    var requestEntity: HttpEntity<*>? = null
    lateinit var responseType: Type

    class Builder {
        private lateinit var url: String
        private lateinit var httpMethod: HttpMethod
        private var httpHeaders: HttpHeaders? = null
        private var request: Any? = null
        private lateinit var responseType: Type
        private var fileRequest = false
        private var filePartName: String? = null
        private var resource: AbstractResource? = null
        private var mimeType: String? = null

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun method(httpMethod: HttpMethod): Builder {
            this.httpMethod = httpMethod
            return this
        }

        fun header(httpHeaders: HttpHeaders): Builder {
            this.httpHeaders = httpHeaders
            return this
        }

        fun header(
            headerName: String,
            headerValue: String
        ): Builder {
            if (httpHeaders == null) {
                httpHeaders = HttpHeaders()
            }
            httpHeaders!![headerName] = headerValue
            return this
        }

        fun header(block: HttpHeaders.() -> Unit): Builder {
            if (httpHeaders == null) {
                httpHeaders = HttpHeaders()
            }
            block(httpHeaders!!)
            return this
        }

        fun body(request: Any?): Builder {
            fileRequest = false
            this.request = request
            return this
        }

        fun fileBody(
            file: File,
            mimeType: String
        ): Builder {
            return fileBody(file, mimeType, null, null)
        }

        fun fileBody(
            file: File,
            mimeType: String,
            params: MultiValueMap<String, Any>?
        ): Builder {
            return fileBody(file, mimeType, params, null)
        }

        fun fileBody(
            file: File,
            mimeType: String,
            params: MultiValueMap<String, Any>?,
            filePartName: String?
        ): Builder {
            fileRequest = true
            resource = FileSystemResource(file)
            request =
                params ?: LinkedMultiValueMap<Any, Any>(0)
            this.mimeType = mimeType
            this.filePartName = filePartName ?: "file"
            return this
        }

        fun fileBody(
            bytes: ByteArray,
            mimeType: String,
            fileName: String
        ): Builder {
            return fileBody(
                bytes,
                mimeType,
                fileName,
                LinkedMultiValueMap(0),
                null
            )
        }

        fun fileBody(
            bytes: ByteArray,
            mimeType: String,
            fileName: String,
            params: MultiValueMap<String, Any>?
        ): Builder {
            return fileBody(bytes, mimeType, fileName, params, null)
        }

        fun fileBody(
            bytes: ByteArray,
            mimeType: String,
            fileName: String,
            params: MultiValueMap<String, Any>?,
            filePartName: String?
        ): Builder {
            fileRequest = true
            val fileSize = bytes.size.toLong()
            resource = object : ByteArrayResource(bytes) {
                override fun contentLength(): Long {
                    return fileSize
                }

                override fun getFilename(): String? {
                    return fileName
                }
            }
            request =
                params ?: LinkedMultiValueMap<Any, Any>(0)
            this.mimeType = mimeType
            this.filePartName = filePartName ?: "file"
            return this
        }

        fun response(responseType: Type): Builder {
            this.responseType = responseType
            return this
        }

        fun build(): Request {
            val request = Request()
            val query = url.substringAfter("?", "")
            if (query.isNotBlank()) {
                //包含query，编码
                val queryArray = query.split("&").toTypedArray()
                val host = url.substringBefore("?", "")
                val array: MutableList<String> =
                    ArrayList(queryArray.size)
                for (s in queryArray) {
                    val key = s.substringBefore("=", "")
                    val value = s.substringAfter("=", "")
                    try {
                        array.add(key + "=" + URLEncoder.encode(value, "UTF-8"))
                    } catch (e: UnsupportedEncodingException) {
                        log.error(
                            "url encode failed, key: {}, value: {}",
                            key,
                            value,
                            e
                        )
                        array.add(s)
                    }
                }
                url = host + array.joinToString(separator = "&", prefix = "?")
            }
            request.uri = URI.create(url)
            request.httpMethod = httpMethod
            buildHeader()
            buildBody(request)
            request.responseType = responseType
            return request
        }

        private fun buildHeader() {
            if (fileRequest) {
                if (httpHeaders == null) {
                    httpHeaders = HttpHeaders()
                }
                httpHeaders!!.contentType = MediaType.MULTIPART_FORM_DATA
            }
        }

        @Suppress("unchecked_cast")
        private fun buildBody(request: Request) {
            if (fileRequest) {
                val map =
                    this.request as MultiValueMap<String?, Any>?
                val fileHeaders = HttpHeaders()
                fileHeaders.contentType = MediaType.parseMediaType(mimeType!!)
                val filePart =
                    HttpEntity(
                        resource,
                        fileHeaders
                    )
                map!!.add(filePartName!!, filePart)
                request.requestEntity =
                    HttpEntity<MultiValueMap<String?, Any>?>(
                        map,
                        httpHeaders
                    )
            } else {
                if (this.request != null && httpHeaders != null) {
                    request.requestEntity =
                        HttpEntity(this.request, httpHeaders)
                } else if (this.request != null) {
                    request.requestEntity = HttpEntity<Any?>(this.request!!)
                } else if (httpHeaders != null) {
                    request.requestEntity = HttpEntity<Any>(httpHeaders!!)
                } else {
                    request.requestEntity = null
                }
            }
        }
    }

    companion object {
        private val log =
            LoggerFactory.getLogger(Request::class.java)
    }
}