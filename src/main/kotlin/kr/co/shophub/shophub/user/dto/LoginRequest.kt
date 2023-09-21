package kr.co.shophub.shophub.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

data class LoginRequest(
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    val email: String,
    @NotBlank
    val password: String,
) {
    fun toAuthenticationToken(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(this.email, this.password)
    }
}