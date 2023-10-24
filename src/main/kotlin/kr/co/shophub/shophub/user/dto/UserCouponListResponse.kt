package kr.co.shophub.shophub.user.dto

import org.springframework.data.domain.Page

class UserCouponListResponse(
    val userCouponList: Page<UserCouponResponse>,
    val couponCount: Int,
) {
}