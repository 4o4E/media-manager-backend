package top.e404.media.annontation

/**
 * 用于标记controller方法在执行前进行权限检查, 需要当前的访问者拥有所有权限才放行
 *
 * @property perms 所有需要的权限
 * @see top.e404.media.advice.PermCheckAdvice
 */
annotation class RequirePerm(vararg val perms: String)
