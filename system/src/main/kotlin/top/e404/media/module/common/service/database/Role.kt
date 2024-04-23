package top.e404.media.module.common.service.database

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import top.e404.media.module.common.annontation.RequirePerm
import top.e404.media.module.common.entity.auth.RoleDo
import top.e404.media.module.common.entity.auth.RolePermDo
import top.e404.media.module.common.mapper.RoleMapper
import top.e404.media.module.common.util.query

interface RoleService : IService<RoleDo> {
    /**
     * 通过用户id查询其角色
     */
    fun getRoleByUserId(userId: Long): List<RoleDo>

    /**
     * 通过用户id查询其权限
     */
    fun getPermByRoleId(id: Long): List<String>

    /**
     * 删除角色, 同时删除给角色分配的权限
     */
    fun remove(id: Long): Boolean

    /**
     * 获取所有权限节点
     */
    fun allPerm(): Set<String>
}

@Service
class RoleServiceImpl : RoleService, ServiceImpl<RoleMapper, RoleDo>() {
    @set:Autowired
    lateinit var rolePermService: RolePermService

    @set:Autowired
    lateinit var applicationContext: WebApplicationContext

    val perms by lazy {
        applicationContext.getBean(RequestMappingHandlerMapping::class.java)
            .handlerMethods
            .values
            .mapNotNull { it.getMethodAnnotation(RequirePerm::class.java) }
            .flatMap { it.perms.asList() }
            .toSet()
    }

    override fun getRoleByUserId(userId: Long): List<RoleDo> {
        return baseMapper.getByUserId(userId)
    }

    override fun getPermByRoleId(id: Long): List<String> {
        return baseMapper.getPermByRoleId(id)
    }

    @Transactional
    override fun remove(id: Long): Boolean {
        rolePermService.remove(query { eq(RolePermDo::role, id) })
        return removeById(id)
    }

    override fun allPerm(): Set<String> = perms
}