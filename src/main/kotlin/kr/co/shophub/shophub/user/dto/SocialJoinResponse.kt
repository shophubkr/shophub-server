package kr.co.shophub.shophub.user.dto

data class SocialJoinResponse (
    val email: String = "",
    val password: String,
    val nickname: String,
    val telNum: String = "",
    val bizNumber: String = "",
)