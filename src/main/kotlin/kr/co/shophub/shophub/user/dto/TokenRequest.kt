package kr.co.shophub.shophub.user.dto

import jakarta.validation.constraints.NotBlank

data class TokenRequest(
    @NotBlank
    val refreshToken: String,
)
