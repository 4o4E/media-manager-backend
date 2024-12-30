package top.e404.media.module.common.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import top.e404.media.module.common.entity.database.RoleDo

@Mapper
interface RoleMapper : BaseMapper<RoleDo> {
    @Select(
        """SELECT r.* FROM sys_role AS r WHERE r.id IN (
            SELECT unnest("user".roles) FROM sys_user AS "user" WHERE "user".id = #{userId}
        )
        """
    )
    fun getByUserId(userId: Long): List<RoleDo>
}