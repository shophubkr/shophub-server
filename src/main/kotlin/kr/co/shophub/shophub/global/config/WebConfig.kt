package kr.co.shophub.shophub.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    companion object {
        private const val ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH"
        private const val ALLOWED_HEADER_NAMES = "Origin, X-Requested-With, Content-Type, Accept, Authorization"
    }
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",
                "http://13.209.100.56",
                "http://13.209.100.56:8081"
            )
            .allowedHeaders(ALLOWED_HEADER_NAMES)
            .allowedMethods(*ALLOWED_METHOD_NAMES.split(",").toTypedArray())
    }
}