package kr.co.shophub.shophub.user.controller.dto.reqeust

import jakarta.validation.constraints.NotBlank

data class TokenRequest(
    @NotBlank
    val refreshToken: String,
)
