package kr.co.shophub.shophub.coupon.controller

import kr.co.shophub.shophub.coupon.dto.*
import kr.co.shophub.shophub.coupon.service.CouponService
import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.global.login.service.LoginService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CouponController(
    private val couponService: CouponService,
    private val loginService: LoginService,
) {

    @PostMapping("/shops/{shopId}/coupons")
    fun createCoupon(
        @PathVariable shopId: Long,
        @RequestBody createCouponRequest: CreateCouponRequest,
    ): CommonResponse<CouponIdResponse> {

        return couponService.createCoupon(
            createCouponRequest = createCouponRequest,
            userId = loginService.getLoginUserId(),
            shopId = shopId,
        ).let { CommonResponse(it) }

    }

    @GetMapping("/shops/{shopId}/coupons")
    fun getCouponList(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
        @PathVariable shopId: Long,
        @RequestParam isTerminated: Boolean = false,
    ): CommonResponse<CouponListResponse> {
        val couponList = couponService.getCouponList(
            shopId = shopId,
            pageable = pageable,
            isTerminated = isTerminated,
        )
        return CommonResponse(
            result = CouponListResponse(couponList.content),
            page = PageInfo.of(page = couponList)
        )
    }

    @GetMapping("/shops/{shopId}/coupons/shortest")
    fun getShortestExpirationCoupon(
        @PathVariable shopId: Long,
    ) :CommonResponse<ShortestExpirationCouponResponse>{
        return CommonResponse(couponService.getShortestExpirationCoupon(shopId))
    }

    @PatchMapping("/coupons/{couponId}")
    fun earlyTerminateCoupon(
        @PathVariable couponId: Long
    ) :CommonResponse<EmptyDto>{

        val userId = loginService.getLoginUserId()
        couponService.terminateCoupon(
            couponId = couponId,
            userId = userId,
        )
        return CommonResponse.EMPTY

    }
}