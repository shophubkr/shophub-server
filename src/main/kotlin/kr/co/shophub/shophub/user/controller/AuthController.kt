package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.service.AuthService
import kr.co.shophub.shophub.user.service.MailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val mailService: MailService,
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

    @GetMapping("/issue")
    fun issueTokenOfOAuthUser(@RequestParam token: String): CommonResponse<TokenResponse> {
        val tokenResponse: TokenResponse = authService.issueTokenOfOAuth(token)
        return CommonResponse(tokenResponse)
    }

    @GetMapping("/add-info")
    fun getAdditionalInfo(@RequestParam token: String): CommonResponse<SocialJoinResponse> {
        return CommonResponse(authService.getAdditionalInfo(token))
    }

    @PostMapping("/add-info")
    fun updateEmailInfo(@RequestBody joinRequest: SocialJoinRequest): CommonResponse<UserResponse> {
        return CommonResponse(authService.updateSocialInfo(joinRequest))
    }

    @GetMapping("/mail")
    fun sendAuthMail(@RequestBody mailAuthRequest: MailAuthRequest) {
        mailService.sendMail(mailAuthRequest)
    }

    @PatchMapping("/authorize")
    fun authorizeEmail(@RequestParam token: String) {
        authService.authorizeEmail(token)
    }

    @GetMapping("/test")
    fun test(): String = "pass"

}