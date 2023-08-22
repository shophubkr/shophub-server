package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.user.controller.dto.reqeust.JoinRequest
import kr.co.shophub.shophub.user.controller.dto.reqeust.LoginRequest
import kr.co.shophub.shophub.user.controller.dto.reqeust.TokenRequest
import kr.co.shophub.shophub.user.controller.dto.response.TokenResponse
import kr.co.shophub.shophub.user.controller.dto.response.UserResponse
import kr.co.shophub.shophub.user.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        val authRequest = loginRequest.toAuthenticationToken()
        val loginResponse = authService.login(authRequest)
        return ResponseEntity.ok(loginResponse.tokenResponse)
    }

    @PostMapping("/join")
    fun join(@RequestBody joinRequest: JoinRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(authService.join(joinRequest))
    }

    @PostMapping("/reissue")
    fun refresh(@RequestBody tokenRequest: TokenRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.reIssueToken(tokenRequest.refreshToken))
    }

    @GetMapping("/test")
    fun test(): String = "pass"

}