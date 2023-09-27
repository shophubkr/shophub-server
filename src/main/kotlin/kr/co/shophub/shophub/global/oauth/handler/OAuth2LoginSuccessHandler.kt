package kr.co.shophub.shophub.global.oauth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.oauth.CustomOAuth2User
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserRole
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class OAuth2LoginSuccessHandler(
    private val userRepository: UserRepository,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val oAuth2User = authentication.principal as CustomOAuth2User
        val user = userRepository.findByEmail(oAuth2User.email) ?: throw ResourceNotFoundException("")

        if (isSignUp(user)) {
            response.sendRedirect("/api/v1/auth/add-info?email=${user.email}")
        } else if (isAlreadyJoin(user)) {
            response.sendRedirect("/api/v1/auth/token?email=${user.email}")
        }
    }

    private fun isSignUp(user: User): Boolean {
        return user.userRole == UserRole.GUEST_SELLER || user.userRole == UserRole.GUEST_BUYER
    }

    private fun isAlreadyJoin(user: User): Boolean {
        val existAlready = userRepository.existsByEmail(user.email)
        val isAuthorized = user.userRole== UserRole.USER
        return existAlready && isAuthorized
    }

}