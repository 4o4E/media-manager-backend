package top.e404.media.module.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
            .exposedHeaders("Authorization")
    }

    @get:Bean
    val corsFilter = CorsFilter(UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            addAllowedOrigin("http://localhost:5173")
            addAllowedHeader("*")
            addAllowedMethod(HttpMethod.GET)
            addAllowedMethod(HttpMethod.POST)
            addAllowedMethod(HttpMethod.PUT)
            addAllowedMethod(HttpMethod.DELETE)
            allowCredentials = true
            maxAge = 3600
        })
    })
}