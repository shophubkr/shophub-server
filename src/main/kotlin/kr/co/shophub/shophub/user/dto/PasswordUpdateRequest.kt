package kr.co.shophub.shophub.user.dto

data class PasswordUpdateRequest(
    val email: String,
    val newPassword: String,
)