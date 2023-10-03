package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.user.model.UserRole

data class SocialJoinRequest (
    val oldEmail: String,
    val newEmail: String,
    val password: String,
    val nickname: String,
    val role: UserRole,

    val phoneNumber: String = "only-seller",
    val bizNumber: String = "only-seller",
)