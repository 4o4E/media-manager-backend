package top.e404.media.module.common.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import top.e404.media.module.common.entity.auth.RoleDo

@Mapper
interface RoleMapper : BaseMapper<RoleDo> {
    @Select(
        """SELECT r.* FROM sys_role r WHERE r.id IN (
        SELECT ur.role_id FROM sys_user_role ur WHERE ur.user_id = #{userId}
    )
    """
    )
    fun getByUserId(userId: Long): List<RoleDo>
}