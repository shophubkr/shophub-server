package kr.co.shophub.shophub.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import kr.co.shophub.shophub.user.model.UserRole

data class JoinRequest(
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    val email: String,
    @NotBlank(message = "비밀번호가 일치하지 않습니다.")
    val password: String,
    @NotBlank(message = "닉네임을 입력해주세요.")
    val nickname: String,
    val role: UserRole,

    val phoneNumber: String = "only-seller",
    val bizNumber: String = "only-seller",
)