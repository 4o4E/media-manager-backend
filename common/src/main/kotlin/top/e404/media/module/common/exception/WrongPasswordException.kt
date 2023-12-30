package top.e404.media.module.common.exception

object WrongPasswordException : CustomMessageException("wrong username or password") {
    private fun readResolve(): Any = WrongPasswordException
    override val toResponseEntity by lazy { super.toResponseEntity }
}