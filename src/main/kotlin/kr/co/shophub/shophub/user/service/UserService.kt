package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.user.controller.dto.SignUpRequest
import kr.co.shophub.shophub.user.domain.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun signUp(request: SignUpRequest): User {
        checkDuplicate(request)
        return User(
            email = request.email,
            password = request.password,
            nickname = request.nickname,
        )
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