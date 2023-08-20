package kr.co.shophub.shophub.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.shophub.shophub.global.jwt.filter.JwtAuthenticationProcessingFilter
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter
import kr.co.shophub.shophub.global.login.handler.LoginFailureHandler
import kr.co.shophub.shophub.global.login.handler.LoginSuccessHandler
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val loginService: LoginService,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
            .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(AntPathRequestMatcher("/error")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/join")).permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter::class.java)
            .addFilterAfter(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun loginSuccessHandler(): LoginSuccessHandler? {
        return LoginSuccessHandler(jwtService, userRepository)
    }

    @Bean
    fun loginFailureHandler(): LoginFailureHandler? {
        return LoginFailureHandler(objectMapper)
    }

    @Bean
    fun authenticationManager(): AuthenticationManager? {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(loginService)
        return ProviderManager(provider)
    }

    @Bean
    fun customJsonUsernamePasswordAuthenticationFilter(): CustomJsonUsernamePasswordAuthenticationFilter? {
        val customJsonUsernamePasswordLoginFilter = CustomJsonUsernamePasswordAuthenticationFilter(objectMapper)
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager())
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler())
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler())
        return customJsonUsernamePasswordLoginFilter
    }

    @Bean
    fun jwtAuthenticationProcessingFilter(): JwtAuthenticationProcessingFilter? {
        return JwtAuthenticationProcessingFilter(jwtService, userRepository)
    }

}