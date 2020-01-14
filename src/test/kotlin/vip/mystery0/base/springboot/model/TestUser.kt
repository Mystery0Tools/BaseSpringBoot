package vip.mystery0.base.springboot.model

/**
 * @author 邓易林
 * Create at 2020/1/11
 */
data class TestUser(
    var name: String,
    var age: Long
) {
    constructor() : this("", 0L)
}