package vip.mystery0.base.springboot.utils.rest.fuse

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

/**
 * @author mystery0
 * Create at 2019/12/28
 */
@Service
class FuseService {
    //熔断10分钟
    private val FUSE_TIME = 10 * 60L
    private val TIME_TO_FUSE = 2

    fun isRequestFailed(statusCode: HttpStatus): Boolean = statusCode != HttpStatus.OK

    //熔断结束时间
    private val FUSE_INTERFACE = ConcurrentHashMap<URI, LocalDateTime>()
    //熔断状态
    private val INTERFACE = ConcurrentHashMap<URI, FuseStatus>()
    //请求失败次数
    private val FAILED_INTERFACE_COUNT = ConcurrentHashMap<URI, Int>()

    fun logSuccess(uri: URI) {
        val count = FAILED_INTERFACE_COUNT[uri]
        if (count == null || count <= 0) {
            FAILED_INTERFACE_COUNT[uri] = 0
            return
        } else {
            FAILED_INTERFACE_COUNT[uri] = FAILED_INTERFACE_COUNT[uri]!! - 1
        }
        changeFuseStatus(uri)
    }

    fun logFailed(uri: URI, statusCode: HttpStatus) {
        if (!isRequestFailed(statusCode)) {
            logSuccess(uri)
            return
        }
        val count = FAILED_INTERFACE_COUNT[uri]
        if (count == null || count <= 0) {
            FAILED_INTERFACE_COUNT[uri] = 1
            return
        } else {
            FAILED_INTERFACE_COUNT[uri] = FAILED_INTERFACE_COUNT[uri]!! + 1
        }
        changeFuseStatus(uri)
    }

    private fun changeFuseStatus(uri: URI) {
        val count = FAILED_INTERFACE_COUNT[uri]
        when {
            count == null || count <= 0 -> {
                //没有熔断过，也就是没有失败过
                INTERFACE[uri] = FuseStatus.CLOSED
                if (count != null) FAILED_INTERFACE_COUNT.remove(uri)
            }
            count in 1 until TIME_TO_FUSE -> {
                //半熔断状态
                INTERFACE[uri] = FuseStatus.HALF_OPEN
            }
            count >= TIME_TO_FUSE -> {
                //熔断状态
                INTERFACE[uri] = FuseStatus.OPEN
                val time = FUSE_INTERFACE[uri] ?: LocalDateTime.MIN
                if (!time.isAfter(LocalDateTime.now())) {
                    //熔断时间已结束，重新设置熔断
                    FUSE_INTERFACE[uri] = LocalDateTime.now().plusSeconds(FUSE_TIME)
                    return
                }
                //熔断时间没结束，不处理
            }
        }
    }
}