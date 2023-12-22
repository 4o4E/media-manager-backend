package top.e404.media.exception

object SimplePasswordException : CustomMessageException("password is too simplistic") {
    private fun readResolve(): Any = SimplePasswordException
    override val toResponseEntity by lazy { super.toResponseEntity }
}