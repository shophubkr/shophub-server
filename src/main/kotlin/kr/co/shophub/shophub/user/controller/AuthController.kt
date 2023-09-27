package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.user.dto.JoinRequest
import kr.co.shophub.shophub.user.dto.LoginRequest
import kr.co.shophub.shophub.user.dto.TokenRequest
import kr.co.shophub.shophub.user.dto.TokenResponse
import kr.co.shophub.shophub.user.dto.UserResponse
import kr.co.shophub.shophub.user.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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

    @GetMapping("/token")
    fun issueTokenOfOAuthUser(@RequestParam email: String): CommonResponse<TokenResponse> {
        val tokenResponse: TokenResponse = authService.issueTokenOfOAuth(email)
        return CommonResponse(tokenResponse)
    }

    /**
     * 소셜로 회원가입 시도시 OAuth에서 가져올 수 있는 내용을 담은 JoinReqeust를 반환한다.
     * 이 반환값으로 화면을 구성하고 아래에 있는 메서드를 통해 누락된 / 수정해야 할 정보를 받아서
     * 회원가입을 완료한다.
     */
    @GetMapping("/add-info")
    fun getAdditionalInfo(@RequestParam email: String): CommonResponse<JoinRequest> {
        return CommonResponse(authService.getAdditionalInfo(email))
    }

    @PostMapping("/add-info")
    fun updateEmailInfo(@RequestParam email: String, @RequestBody joinRequest: JoinRequest): CommonResponse<UserResponse> {
        return CommonResponse(authService.updateEmailInfo(joinRequest, email))
    }

    /**
     * 이메일 인증 부분 -> 아직 userId를 통해 인증을 구현한다.
     * GUEST_BUYER -> USER
     * GUEST_SELLER -> SELLER
     * 위의 방식으로 UserRole을 업데이트 한다.
     */
    @PatchMapping("/update/{userId}")
    fun updateRole(@PathVariable userId: Long) {
        authService.updateRole(userId)
    }

    @GetMapping("/test")
    fun test(): String = "pass"

}