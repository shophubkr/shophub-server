package kr.co.shophub.shophub.user.dto

data class InfoUpdateRequest (
    val nickname: String,
    val oldPassword: String,
    val newPassword: String,
)