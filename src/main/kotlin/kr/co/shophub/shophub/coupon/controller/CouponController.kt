package kr.co.shophub.shophub.coupon.controller

import kr.co.shophub.shophub.coupon.dto.CouponIdResponse
import kr.co.shophub.shophub.coupon.dto.CouponListResponse
import kr.co.shophub.shophub.coupon.dto.CouponResponse
import kr.co.shophub.shophub.coupon.dto.CreateCouponRequest
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
@RequestMapping("/api/v1/coupon")
class CouponController(
    private val couponService: CouponService,
    private val loginService: LoginService,
) {

    @PostMapping("/{shopId}")
    fun createCoupon(
        @PathVariable shopId: Long,
        @RequestBody createCouponRequest: CreateCouponRequest,
    ): CommonResponse<CouponIdResponse> {
        return couponService.createCoupon(
            createCouponRequest = createCouponRequest,
            userId = loginService.getLoginUserId(),
            shopId = shopId
        ).let { CommonResponse(it) }
    }

    @GetMapping("/{shopId}")
    fun getCouponList(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
        @PathVariable shopId: Long,
        @RequestParam isFinished: Boolean,
    ): CommonResponse<CouponListResponse> {
        val couponList = couponService.getCouponList(
            shopId = shopId,
            pageable = pageable,
            isFinished = isFinished,
        )
        return CommonResponse(
            result = CouponListResponse(couponList.content.map { CouponResponse(it) }),
            page = PageInfo.of(page = couponList)
        )
    }

    @PatchMapping("/{couponId}")
    fun earlyTerminateCoupon(
        @PathVariable couponId: Long
    ) :CommonResponse<EmptyDto>{
        val userId = loginService.getLoginUserId()
        couponService.terminateCoupon(
            couponId = couponId,
            userId = userId
        )
        return CommonResponse.EMPTY
    }
}