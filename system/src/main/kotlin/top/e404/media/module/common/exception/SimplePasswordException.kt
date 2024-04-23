package top.e404.media.module.common.exception

/**
 * 密码过于简单
 */
object SimplePasswordException : CustomMessageException("密码过于简单") {
    private fun readResolve(): Any = SimplePasswordException
    override val toResponseEntity by lazy { super.toResponseEntity }
}