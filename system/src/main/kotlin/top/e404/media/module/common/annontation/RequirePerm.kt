package top.e404.media.module.common.annontation

import top.e404.media.module.common.enums.SysPerm

/**
 * 用于标记controller方法在执行前进行权限检查, 需要当前的访问者拥有所有权限才放行
 *
 * @property perms 所有需要的权限
 * @see top.e404.media.module.common.advice.PermCheckAdvice
 */
annotation class RequirePerm(vararg val perms: SysPerm)
