package kr.co.shophub.shophub.user.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)