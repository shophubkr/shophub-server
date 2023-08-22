package kr.co.shophub.shophub.user.controller.dto.response

data class LoginResponse(
    val tokenResponse: TokenResponse,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)