package top.e404.media.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import top.e404.media.entity.auth.UserBindDo

@Mapper
interface UserBindMapper : BaseMapper<UserBindDo>