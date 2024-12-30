package top.e404.media.module.common.entity

data class BaseResp<T : Any>(
    val code: Int,
    val success: Boolean,
    val message: String,
    val data: T?
) {
    companion object {
        fun ok() = BaseResp(0, true, "OK", null)
        fun fail(code: Int, message: String): BaseResp<Unit> = BaseResp(code, false, message, null)
    }
}

fun <T : Any> resp(data: T) = BaseResp(0, true, "OK", data)
fun <T : Any> T.toResp() = BaseResp(0, true, "OK", this)