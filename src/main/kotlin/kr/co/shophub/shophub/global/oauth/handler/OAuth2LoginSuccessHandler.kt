package kr.co.shophub.shophub.global.oauth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.global.oauth.CustomOAuth2User
import kr.co.shophub.shophub.user.model.UserRole
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as CustomOAuth2User
        val tokenForOAuth2 = jwtService.createTokenForOAuth2(oAuth2User.email)

        if (isNewJoin(oAuth2User)) {
            response.sendRedirect("/api/v1/auth/add-info?token=$tokenForOAuth2")
        }
        if (isAlreadyJoin(oAuth2User)) {
            response.sendRedirect("/api/v1/auth/issue?token=$tokenForOAuth2")
        }
    }

    private fun isNewJoin(user: CustomOAuth2User): Boolean {
        return user.role == UserRole.SOCIAL_GUEST
    }

    private fun isAlreadyJoin(user: CustomOAuth2User): Boolean {
        val existAlready = userRepository.existsByEmail(user.email)
        val isAuthorized = user.role == UserRole.USER_BUYER || user.role == UserRole.USER_SELLER
        return existAlready && isAuthorized
    }

}