package kr.co.shophub.shophub.global.config

import net.gpedro.integrations.slack.SlackApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("ec2-dev")
class SlackLogAppenderConfig {

    @Value("\${logging.slack.webhook-uri}")
    lateinit var token: String

    @Bean
    fun slackApi(): SlackApi {
        return SlackApi(token)
    }
}