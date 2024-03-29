package top.e404.media.module.common.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import top.e404.media.module.common.entity.auth.UserTokenDo

@Mapper
interface UserTokenMapper : BaseMapper<UserTokenDo>