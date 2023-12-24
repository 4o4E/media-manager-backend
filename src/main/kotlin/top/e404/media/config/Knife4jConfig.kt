package top.e404.media.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Knife4jConfig {
    @get:Bean
    val adminApi: GroupedOpenApi = GroupedOpenApi.builder()
        .group("media-manager")
        .pathsToMatch("/**")
        .build()

    @get:Bean
    val openAPI: OpenAPI = OpenAPI().info(Info()
        .title("media-manager")
        .description("media-manager")
        .version("1.0"))
}
