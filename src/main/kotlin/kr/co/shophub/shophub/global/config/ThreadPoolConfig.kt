package kr.co.shophub.shophub.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor


@EnableAsync
@Configuration
class ThreadPoolConfig {
    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.maxPoolSize = 5 //최대 스레드 수
        threadPoolTaskExecutor.corePoolSize = 5 //기본 스레드 수
        threadPoolTaskExecutor.initialize()
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-")
        return threadPoolTaskExecutor
    }
}