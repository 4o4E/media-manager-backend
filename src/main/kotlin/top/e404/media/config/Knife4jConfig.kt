package top.e404.media.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Knife4jConfig {
    @Bean
    fun adminApi(): GroupedOpenApi {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("media")         // 分组名称
                .pathsToMatch("/**")  // 接口请求路径规则
                .build()
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
                .info(
                        Info()
                                .title("我和我的队员真厉害")
                                .description("媒体应用")
                                .version("v1")
                                .contact(Contact().name("404e, ahao").email("*******@gmail.com"))
                                .license(License().name("Apache 2.0").url("http://springdoc.org"))
                )
    }
}
