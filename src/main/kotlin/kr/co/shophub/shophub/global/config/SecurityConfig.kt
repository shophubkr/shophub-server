package kr.co.shophub.shophub.global.config

import kr.co.shophub.shophub.global.jwt.error.JwtAccessDeniedHandler
import kr.co.shophub.shophub.global.jwt.error.JwtAuthenticationEntryPoint
import kr.co.shophub.shophub.global.jwt.filter.JwtAuthenticationProcessingFilter
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.global.oauth.handler.OAuth2LoginFailureHandler
import kr.co.shophub.shophub.global.oauth.handler.OAuth2LoginSuccessHandler
import kr.co.shophub.shophub.global.oauth.service.CustomOAuth2UserService
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val loginService: LoginService,
    private val jwtService: JwtService,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
    private val oAuth2LoginFailureHandler: OAuth2LoginFailureHandler,
    private val customOAuth2UserService: CustomOAuth2UserService,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
            .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers(AntPathRequestMatcher("/")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/error")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/test/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/api/v1/users/mail/password")).permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2Login {
                it.successHandler(oAuth2LoginSuccessHandler)
                it.failureHandler(oAuth2LoginFailureHandler)
                it.userInfoEndpoint { user -> user
                    .userService(customOAuth2UserService)
                }
            }
            .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager? {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(loginService)
        return ProviderManager(provider)
    }

    @Bean
    fun jwtAuthenticationProcessingFilter(): JwtAuthenticationProcessingFilter? {
        return JwtAuthenticationProcessingFilter(jwtService)
    }

}