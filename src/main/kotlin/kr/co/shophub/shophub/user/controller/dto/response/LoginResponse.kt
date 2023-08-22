package kr.co.shophub.shophub.user.controller.dto.response

import org.springframework.http.HttpStatus

data class LoginResponse(
    val tokenResponse: TokenResponse,
    val httpStatus: HttpStatus,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)