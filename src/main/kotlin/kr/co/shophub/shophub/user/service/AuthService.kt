package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.dto.JoinRequest
import kr.co.shophub.shophub.user.dto.TokenResponse
import kr.co.shophub.shophub.user.dto.UserResponse
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserRole
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

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
        val userRole = checkRole(request.role)
        val user = User(
            email = request.email,
            password = request.password,
            nickname = request.nickname,
            userRole = userRole,
        )
        user.encodePassword(passwordEncoder)
        return UserResponse.toResponse(userRepository.save(user))
    }

    private fun checkRole(role: UserRole): UserRole {
        return if (role == UserRole.USER) {
            UserRole.GUEST_BUYER
        } else {
            UserRole.GUEST_SELLER
        }
    }

    private fun checkDuplicate(request: JoinRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("이미 가입한 이메일 입니다.")
        }
        if (userRepository.existsByNickname(request.nickname)) {
            throw IllegalStateException("이미 가입한 닉네임 입니다.")
        }
    }

    @Transactional
    fun login(authRequest: UsernamePasswordAuthenticationToken): TokenResponse {
        val authentication = authenticationManager.authenticate(authRequest)
        val email = authentication.name
        return jwtService.makeTokenResponse(email)
    }

    @Transactional
    fun reIssueToken(refreshToken: String): TokenResponse {
        checkToken(refreshToken)
        val user = userRepository.findByRefreshToken(refreshToken) ?: throw IllegalArgumentException("유저가 존재 하지 않습니다.")
        return jwtService.makeTokenResponse(user.email)
    }

    @Transactional
    fun issueTokenOfOAuth(email: String): TokenResponse {
        return jwtService.makeTokenResponse(email)
    }

    private fun checkToken(refreshToken: String) {
        require(jwtService.isTokenValid(refreshToken)) { "토큰이 유효하지 않습니다." }
    }

    fun getAdditionalInfo(email: String): JoinRequest {
        val noEmailUser = userRepository.findByEmail(email) ?: throw IllegalArgumentException("유저가 존재 하지 않습니다.")
        var emailValue = noEmailUser.email
        if (noEmailUser.email.startsWith("no-kakao-email")) {
            emailValue = ""
        }
        return JoinRequest(
            email = emailValue,
            password = noEmailUser.password,
            nickname = noEmailUser.nickname,
            role = noEmailUser.userRole,
        )
    }

    @Transactional
    fun updateEmailInfo(joinRequest: JoinRequest, newEmail: String): UserResponse {
        val oldEmailUser = userRepository.findByEmail(joinRequest.email) ?: throw IllegalArgumentException("유저가 존재 하지 않습니다.")
        oldEmailUser.updateEmail(newEmail)
        return UserResponse(oldEmailUser.id)
    }

    @Transactional
    fun updateRole(userId: Long) {
        val user = userRepository.findById(userId).getOrNull() ?: throw IllegalArgumentException()
        user.updateRole()
    }

}