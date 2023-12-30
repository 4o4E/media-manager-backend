package top.e404.media.module.common.exception

object SimplePasswordException : CustomMessageException("password is too simplistic") {
    private fun readResolve(): Any = SimplePasswordException
    override val toResponseEntity by lazy { super.toResponseEntity }
}