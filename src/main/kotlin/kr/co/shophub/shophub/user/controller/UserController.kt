package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.user.controller.dto.reqeust.JoinRequest
import kr.co.shophub.shophub.user.controller.dto.reqeust.LoginRequest
import kr.co.shophub.shophub.user.controller.dto.response.TokenResponse
import kr.co.shophub.shophub.user.controller.dto.response.UserResponse
import kr.co.shophub.shophub.user.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        val authRequest = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        val loginResponse = authService.login(authRequest)
        return ResponseEntity.ok(
            loginResponse.tokenResponse
        )
    }

    @PostMapping("/join")
    fun signUp(@RequestBody request: JoinRequest): ResponseEntity<UserResponse> {
        val userResponse = authService.join(request)
        return ResponseEntity.ok(userResponse)
    }

    @PostMapping("/reissue-refresh")
    fun refresh(@RequestBody refreshToken: String, authentication: Authentication): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.reIssueToken(refreshToken, authentication))
    }

    @GetMapping("/test")
    fun test(): String {
        return "pass"
    }

}