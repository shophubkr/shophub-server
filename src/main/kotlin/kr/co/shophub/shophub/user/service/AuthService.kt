package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.controller.dto.reqeust.JoinRequest
import kr.co.shophub.shophub.user.controller.dto.response.LoginResponse
import kr.co.shophub.shophub.user.controller.dto.response.TokenResponse
import kr.co.shophub.shophub.user.controller.dto.response.UserResponse
import kr.co.shophub.shophub.user.domain.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    @Transactional
    fun join(request: JoinRequest): UserResponse {
        checkDuplicate(request)
        val user = User(
            email = request.email,
            password = request.password,
            nickname = request.nickname,
        )

        user.encodePassword(passwordEncoder)

        return UserResponse.toResponse(userRepository.save(user))
    }

    private fun checkDuplicate(request: JoinRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 가입한 이메일 입니다.")
        }
        if (userRepository.existsByNickname(request.nickname)) {
            throw IllegalArgumentException("이미 가입한 닉네임 입니다.")
        }
    }

    @Transactional
    fun login(authRequest: UsernamePasswordAuthenticationToken): LoginResponse {
        val authentication = authenticationManager.authenticate(authRequest)
        val email = authentication.name
        val tokenResponse = jwtService.getTokenResponse(email)
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException()
        user.updateRefreshToken(tokenResponse.refreshToken)
        return LoginResponse(
            tokenResponse,
            HttpStatus.OK
        )
    }

    @Transactional
    fun reIssueToken(refreshToken: String, authentication: Authentication): TokenResponse {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw IllegalArgumentException()
        }
        val tokenResponse = jwtService.getTokenResponse(authentication.name)
        val user = userRepository.findByRefreshToken(refreshToken) ?: throw IllegalArgumentException()
        user.updateRefreshToken(tokenResponse.refreshToken)
        return tokenResponse
    }

}