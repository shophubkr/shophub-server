package kr.co.shophub.shophub.user.controller.dto.response

import kr.co.shophub.shophub.user.domain.User

data class UserResponse(
    val email: String,
    val nickname: String,
) {
    companion object {
        fun toResponse(user: User): UserResponse {
            return UserResponse(user.email, user.nickname)
        }
    }
}