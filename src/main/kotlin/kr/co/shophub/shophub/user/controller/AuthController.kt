package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.user.dto.JoinRequest
import kr.co.shophub.shophub.user.dto.LoginRequest
import kr.co.shophub.shophub.user.dto.TokenRequest
import kr.co.shophub.shophub.user.dto.TokenResponse
import kr.co.shophub.shophub.user.dto.UserResponse
import kr.co.shophub.shophub.user.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): CommonResponse<TokenResponse> {
        val authRequest = loginRequest.toAuthenticationToken()
        return CommonResponse(authService.login(authRequest))
    }

    @PostMapping("/join")
    fun join(@RequestBody joinRequest: JoinRequest): CommonResponse<UserResponse> {
        return CommonResponse(authService.join(joinRequest))
    }

    @PostMapping("/reissue")
    fun refresh(@RequestBody tokenRequest: TokenRequest): CommonResponse<TokenResponse> {
        return CommonResponse(authService.reIssueToken(tokenRequest.refreshToken))
    }

    @GetMapping("/code/{typeDescription}")
    fun ss(@RequestParam refreshToken: String, @PathVariable typeDescription: String): CommonResponse<TokenResponse> {
        return CommonResponse(authService.reIssueToken(refreshToken))
    }

    @GetMapping("/test")
    fun test(): String = "pass"

}