package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.service.MailService
import kr.co.shophub.shophub.user.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val loginService: LoginService,
    private val userService: UserService,
    private val mailService: MailService,
) {

    @GetMapping("/my")
    fun myPage(): CommonResponse<MyPageResponse> {
        val userId = getLoginId()
        return CommonResponse(userService.getMyPage(userId))
    }

    @PatchMapping("/update")
    fun updateMyInfo(@RequestBody updateRequest: InfoUpdateRequest): CommonResponse<EmptyDto> {
        val userId = getLoginId()
        userService.updateInfo(userId, updateRequest)
        return CommonResponse.EMPTY
    }

    @GetMapping("/check-password")
    fun checkPassword(@RequestBody password: PasswordRequest): CommonResponse<String> {
        val userId = getLoginId()
        return CommonResponse(userService.checkPassword(password, userId))
    }

    @GetMapping("/mail/password")
    fun sendMailForPassword(@RequestBody mailRequest: MailRequest): CommonResponse<EmptyDto> {
        mailService.sendMailForPassword(mailRequest)
        return CommonResponse.EMPTY
    }

    @PatchMapping("/update/password")
    fun updatePassword(@RequestBody passwordUpdateRequest: PasswordUpdateRequest): CommonResponse<EmptyDto> {
        userService.updatePassword(passwordUpdateRequest)
        return CommonResponse.EMPTY
    }

    @DeleteMapping
    fun deleteUser(): CommonResponse<EmptyDto> {
        val userId = getLoginId()
        userService.deleteUser(userId)
        return CommonResponse.EMPTY
    }

    private fun getLoginId(): Long {
        return loginService.getLoginUserId()
    }
}