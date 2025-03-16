package top.e404.media.module.common.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import top.e404.media.module.common.entity.database.RoleDo
import top.e404.media.module.common.entity.typeHandler.VarcharListTypeHandler

@Mapper
interface RoleMapper : BaseMapper<RoleDo> {
    @Select(
        """SELECT r.* FROM sys_role AS r WHERE r.id IN (
            SELECT unnest("user".roles) FROM sys_user AS "user" WHERE "user".id = #{userId}
        )
        """
    )
    @Results(
        value = [
            Result(column = "perms", property = "perms", typeHandler = VarcharListTypeHandler::class)
        ]
    )
    fun getByUserId(userId: Long): List<RoleDo>
}