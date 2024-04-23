package top.e404.media.module.common.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 配置分页插件
 */
@Configuration
class MybatisPageConfig {
    @get:Bean
    val mybatisPlusInterceptor = MybatisPlusInterceptor().apply {
        addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
    }
}