package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.user.model.UserRole

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val userRole: UserRole
)