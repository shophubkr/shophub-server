package kr.co.shophub.shophub.global.oauth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.global.oauth.CustomOAuth2User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class OAuth2LoginSuccessHandlerV2(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val oAuth2User = authentication.principal as CustomOAuth2User
        val email = oAuth2User.email
        val tokenResponse = jwtService.makeTokenResponse(email)
        val user = userRepository.findByEmail(email) ?: throw ResourceNotFoundException("해당 유저를 찾을 수 없습니다.")
        user.updateRefreshToken(tokenResponse.refreshToken)
        val tokenValue = objectMapper.writeValueAsString(CommonResponse(tokenResponse))

        response.writer.write(tokenValue)
    }

}