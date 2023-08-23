package kr.co.shophub.shophub.user.controller.dto.response

import kr.co.shophub.shophub.user.domain.User

data class UserResponse(
    val id: Long,
) {
    companion object {
        fun toResponse(user: User): UserResponse {
            return UserResponse(user.id)
        }
    }
}