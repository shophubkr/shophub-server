package kr.co.shophub.shophub.global.login.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.transaction.annotation.Transactional

@Transactional
class LoginSuccessHandler(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val userDetails = authentication.principal as UserDetails
        val accessToken = jwtService.createAccessToken(userDetails.username)
        val refreshToken = jwtService.createRefreshToken()
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken)

        val user = userRepository.findByEmail(userDetails.username)
            ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        user.updateRefreshToken(refreshToken)
    }
}