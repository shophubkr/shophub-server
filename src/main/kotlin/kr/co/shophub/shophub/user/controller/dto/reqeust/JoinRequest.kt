package kr.co.shophub.shophub.user.controller.dto.reqeust

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class JoinRequest(
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    val email: String,
    @NotBlank(message = "비밀번호가 일치하지 않습니다.")
    val password: String,
    @NotBlank(message = "닉네임을 입력해주세요.")
    val nickname: String,

    val phoneNumber: String = "only-seller",
    val bizNumber: String = "only-seller",
)