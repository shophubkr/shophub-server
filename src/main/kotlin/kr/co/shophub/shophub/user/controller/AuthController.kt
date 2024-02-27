package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.service.AuthService
import org.springframework.web.bind.annotation.*

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
    fun join(@RequestBody joinRequest: JoinRequest): CommonResponse<TokenResponse> {
        return CommonResponse(authService.join(joinRequest))
    }

    @PostMapping("/reissue")
    fun reIssueToken(@RequestBody tokenRequest: TokenRequest): CommonResponse<TokenResponse> {
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

    @PostMapping("/social-info")
    fun updateEmailInfo(@RequestBody joinRequest: SocialJoinRequest): CommonResponse<UserResponse> {
        return CommonResponse(authService.updateSocialInfo(joinRequest))
    }

}