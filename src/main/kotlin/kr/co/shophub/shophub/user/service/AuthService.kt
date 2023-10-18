package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.event.JoinEvent
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserRole
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val publisher: ApplicationEventPublisher,
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
        checkEmail(user.email)
        checkNickname(user.nickname)
        user.encodePassword(passwordEncoder)
        publisher.publishEvent(JoinEvent(user.email))
        return UserResponse.toResponse(userRepository.save(user))
    }

    private fun checkRole(role: UserRole): UserRole {
        return if (role == UserRole.USER) {
            UserRole.GUEST_BUYER
        } else {
            UserRole.GUEST_SELLER
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
        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
        return jwtService.makeTokenResponse(user.email)
    }

    @Transactional
    fun issueTokenOfOAuth(token: String): TokenResponse {
        checkToken(token)
        val email = jwtService.extractEmail(token)
            ?: throw IllegalStateException("토큰이 올바르지 않습니다.")
        return jwtService.makeTokenResponse(email)
    }

    private fun checkToken(refreshToken: String) {
        require(jwtService.isTokenValid(refreshToken)) { "토큰이 유효하지 않습니다." }
    }

    @Transactional(readOnly = true)
    fun getAdditionalInfo(token: String): SocialJoinResponse {
        checkToken(token)
        val email = jwtService.extractEmail(token)
            ?: throw IllegalStateException("토큰이 올바르지 않습니다.")
        val socialUser = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
        checkOAuthDuplicate(socialUser)
        return SocialJoinResponse(
            email = socialUser.email,
            password = socialUser.password,
            nickname = socialUser.nickname,
        )
    }

    @Transactional
    fun updateSocialInfo(socialJoinRequest: SocialJoinRequest): UserResponse {
        val oldEmailUser = userRepository.findByEmail(socialJoinRequest.oldEmail)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
        checkEmail(socialJoinRequest.newEmail)
        oldEmailUser.updateSocialInfo(socialJoinRequest, checkRole(socialJoinRequest.role))
        publisher.publishEvent(JoinEvent(socialJoinRequest.newEmail))
        return UserResponse(oldEmailUser.id)
    }

    @Transactional
    fun authorizeEmail(token: String) {
        val email = jwtService.extractEmail(token)
            ?: throw IllegalStateException("토큰이 올바르지 않습니다.")
        val user = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
        user.updateRole()
    }

    private fun checkOAuthDuplicate(user: User) {
        if (isAuthenticated(user)) {
            checkEmail(user.email)
            checkNickname(user.nickname)
        }
    }

    private fun checkEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw IllegalStateException("이미 가입한 이메일 입니다.")
        }
    }

    private fun checkNickname(nickname: String) {
        if (userRepository.existsByNickname(nickname)) {
            throw IllegalStateException("이미 가입한 닉네임 입니다.")
        }
    }

    private fun isAuthenticated(user: User) =
        user.userRole == UserRole.SELLER || user.userRole == UserRole.USER

}