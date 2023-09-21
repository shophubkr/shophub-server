package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.user.model.User

data class UserResponse(
    val id: Long,
) {
    companion object {
        fun toResponse(user: User): UserResponse {
            return UserResponse(user.id)
        }
    }
}