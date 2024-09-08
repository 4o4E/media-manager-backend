package top.e404.media.module.common.entity

interface BaseResponse<T : Any> {
    /**
     * 是否成功
     */
    val success: Boolean

    /**
     * 若失败则返回错误信息
     */
    val message: String

    /**
     * 若成功则返回数据
     */
    val data: T?
}

data class BaseResp<T : Any>(
    override val success: Boolean,
    override val message: String,
    override val data: T?
) : BaseResponse<T>

fun <T : Any> resp(data: T) = BaseResp(true, "OK", data)
fun <T : Any> T.toResp() = BaseResp(true, "OK", this)

fun fail(message: String): BaseResp<Unit> = BaseResp(false, message, null)