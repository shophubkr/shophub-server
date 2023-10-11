package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.model.UserCouponCond
import kr.co.shophub.shophub.user.service.MailService
import kr.co.shophub.shophub.user.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/my/coupon")
    fun myCoupons(
        @RequestParam status: UserCouponCond,
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): CommonResponse<UserCouponListResponse>{
        val userId = getLoginId()
        val myCoupons = userService.getMyCoupons(userId, status, pageable)
        return CommonResponse(myCoupons)
    }

    @PatchMapping("/update")
    fun updateMyInfo(@RequestBody updateRequest: InfoUpdateRequest): CommonResponse<EmptyDto> {
        val userId = getLoginId()
        userService.updateInfo(userId, updateRequest)
        return CommonResponse.EMPTY
    }

    @GetMapping("/check-password")
    fun checkPassword(@RequestBody password: PasswordRequest): CommonResponse<EmptyDto> {
        val userId = getLoginId()
        userService.checkPassword(password, userId)
        return CommonResponse.EMPTY
    }

    @GetMapping("/mail/password")
    fun sendMailForPassword(@RequestBody mailRequest: MailRequest): CommonResponse<EmptyDto> {
        mailService.sendPasswordMail(mailRequest)
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

    @PostMapping("/{couponId}")
    fun receiveCoupon(
        @PathVariable couponId: Long
    ): CommonResponse<UserCouponIdResponse> {
        val userId = getLoginId()
        return CommonResponse(userService.receiveCoupon(couponId, userId))
    }

    @PatchMapping("/{userCouponId}")
    fun useCoupon(
        @PathVariable userCouponId: Long
    ): CommonResponse<EmptyDto> {
        userService.useCoupon(userCouponId)
        return CommonResponse.EMPTY
    }

    private fun getLoginId(): Long {
        return loginService.getLoginUserId()
    }
}