package kr.co.shophub.shophub.global.login.service

import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {

        val user = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("해당 이메일이 존재하지 않습니다.")

        return User.builder()
            .username(email)
            .password(user.password)
            .roles(user.userRole.name)
            .build()
    }

}