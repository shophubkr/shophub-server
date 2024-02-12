package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.coupon.dto.CouponListResponse
import kr.co.shophub.shophub.coupon.dto.CouponResponse
import kr.co.shophub.shophub.coupon.service.CouponService
import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.shop.service.ShopService
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.model.UserCouponCond
import kr.co.shophub.shophub.user.model.UserRole
import kr.co.shophub.shophub.user.service.MailService
import kr.co.shophub.shophub.user.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val loginService: LoginService,
    private val userService: UserService,
    private val mailService: MailService,
    private val shopService: ShopService,
    private val couponService: CouponService,
    private val clock: Clock
) {

    @GetMapping("/me/buyer")
    fun buyerInfo(): CommonResponse<BuyerPageResponse> {
        val userId = getLoginId()
        return CommonResponse(userService.getMyPage(userId))
    }

    @GetMapping("/me/seller")
    fun sellerInfo(): CommonResponse<SellerPageResponse> {
        val userId = getLoginId()
        val myCoupons = couponService.getMyCoupons(userId)
        return CommonResponse(SellerPageResponse(
            userInfo = userService.getUserInfo(userId, UserRole.SELLER),
            myShop = shopService.getMyShopList(userId),
            myCoupon = CouponListResponse(myCoupons.map { CouponResponse(it, clock) })
        ))
    }

    @GetMapping("/me/coupons")
    fun myCoupons(
        @RequestParam status: UserCouponCond,
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): CommonResponse<UserCouponListResponse>{
        val userId = getLoginId()
        val myCoupons = userService.getMyCoupons(userId, status, pageable, LocalDate.now(clock))
        return CommonResponse(myCoupons)
    }

    @PatchMapping("/me/info")
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

    @PatchMapping("/update-password")
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

    @PostMapping("/coupons/{couponId}")
    fun receiveCoupon(
        @PathVariable couponId: Long
    ): CommonResponse<UserCouponIdResponse> {
        val userId = getLoginId()
        return CommonResponse(userService.receiveCoupon(couponId, userId))
    }

    @PatchMapping("/coupons/{couponId}")
    fun useCoupon(
        @PathVariable couponId: Long
    ): CommonResponse<EmptyDto> {
        userService.useCoupon(couponId)
        return CommonResponse.EMPTY
    }

    @GetMapping("/me/simple-info")
    fun getSimpleInfo(): CommonResponse<SimpleInfoResponse> {
        val userId = getLoginId()
        return CommonResponse(userService.findSimpleInfo(userId))
    }

    @GetMapping("/check-nickname")
    fun checkNickname(@RequestBody nicknameRequest: NicknameRequest): CommonResponse<EmptyDto> {
        userService.checkNickname(nicknameRequest)
        return CommonResponse.EMPTY
    }

    private fun getLoginId(): Long {
        return loginService.getLoginUserId()
    }
}