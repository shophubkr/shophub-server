package kr.co.shophub.shophub.global.login

import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils
{
    fun getLoginUserId(userRepository : UserRepository) : Long{
        return (SecurityContextHolder.getContext().authentication?.name            ?.let { userRepository.findByEmail(it) }
            ?: throw ResourceNotFoundException("해당 계정이 존재하지 않습니다."))
            .id
    }
}