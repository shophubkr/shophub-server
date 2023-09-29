package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.dto.*
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
        val userRole = checkRole(request.role)
        val user = User(
            email = request.email,
            password = request.password,
            nickname = request.nickname,
            userRole = userRole,
        )
        checkDuplicate(user)
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

    private fun checkDuplicate(user: User) {
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalStateException("이미 가입한 이메일 입니다.")
        }
        if (userRepository.existsByNickname(user.nickname)) {
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
    fun issueTokenOfOAuth(token: String): TokenResponse {
        checkToken(token)
        val email = jwtService.extractEmail(token) ?: throw IllegalStateException("")
        return jwtService.makeTokenResponse(email)
    }

    private fun checkToken(refreshToken: String) {
        require(jwtService.isTokenValid(refreshToken)) { "토큰이 유효하지 않습니다." }
    }

    fun getAdditionalInfo(token: String): SocialJoinResponse {
        checkToken(token)
        val email = jwtService.extractEmail(token) ?: throw IllegalStateException("")
        val socialUser = userRepository.findByEmail(email) ?: throw IllegalArgumentException("유저가 존재 하지 않습니다.")
        checkDuplicate(socialUser)
        return SocialJoinResponse(
            email = socialUser.email,
            password = socialUser.password,
            nickname = socialUser.nickname,
        )
    }


    @Transactional
    fun updateEmailInfo(socialJoinRequest: SocialJoinRequest): UserResponse {
        val oldEmailUser = userRepository.findByEmail(socialJoinRequest.oldEmail) ?: throw IllegalArgumentException("유저가 존재 하지 않습니다.")
        oldEmailUser.updateEmail(socialJoinRequest)
        checkDuplicate(oldEmailUser)
        return UserResponse(oldEmailUser.id)
    }

    @Transactional
    fun updateRole(userId: Long) {
        val user = userRepository.findById(userId).getOrNull() ?: throw IllegalArgumentException()
        user.updateRole()
    }

}