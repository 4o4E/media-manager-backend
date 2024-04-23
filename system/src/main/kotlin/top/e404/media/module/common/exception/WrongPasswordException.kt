package top.e404.media.module.common.exception

/**
 * 用户名或密码错误
 */
object WrongPasswordException : CustomMessageException("用户名或密码错误") {
    private fun readResolve(): Any = WrongPasswordException
    override val toResponseEntity by lazy { super.toResponseEntity }
}