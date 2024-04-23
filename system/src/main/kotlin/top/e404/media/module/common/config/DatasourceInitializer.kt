package top.e404.media.module.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import java.io.File
import javax.sql.DataSource

/**
 * 初始化数据库
 */
@Configuration
class CustomizeDataSourceInitializer {
    @Value("classpath:DDL.sql")
    lateinit var sqlScriptSchema: Resource

    @Bean
    fun dataSourceInitializer(dataSource: DataSource) = DataSourceInitializer().apply {
        setDataSource(dataSource)
        if (!File(InitRunner.INIT_FILE_NAME).exists()) setDatabasePopulator(ResourceDatabasePopulator().apply {
            addScript(
                sqlScriptSchema
            )
        })
    }
}