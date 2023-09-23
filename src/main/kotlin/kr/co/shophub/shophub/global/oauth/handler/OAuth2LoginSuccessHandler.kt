package kr.co.shophub.shophub.global.oauth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.global.oauth.CustomOAuth2User
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class OAuth2LoginSuccessHandler(
    private val jwtService: JwtService,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val oAuth2User = authentication.principal as CustomOAuth2User
        val email = oAuth2User.email
        val type = oAuth2User.type
        val tokenResponse = jwtService.makeTokenResponse(email)
        response.sendRedirect("/api/v1/auth/code/${type.description}?refreshToken=${tokenResponse.refreshToken}")

    }

}