package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.user.controller.dto.reqeust.SignUpRequest
import kr.co.shophub.shophub.user.controller.dto.response.UserResponse
import kr.co.shophub.shophub.user.domain.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signUp(request: SignUpRequest): UserResponse {
        checkDuplicate(request)
        val user = User(
            email = request.email,
            password = request.password,
            nickname = request.nickname,
        )

        user.encodePassword(passwordEncoder)

        return UserResponse.toResponse(userRepository.save(user))
    }

    private fun checkDuplicate(request: SignUpRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 가입한 이메일 입니다.")
        }
        if (userRepository.existsByNickname(request.nickname)) {
            throw IllegalArgumentException("이미 가입한 닉네임 입니다.")
        }
    }

}