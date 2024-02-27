package kr.co.shophub.shophub.user.dto

import org.springframework.data.domain.Page

data class UserCouponListResponse(
    val userCouponList: Page<UserCouponResponse>,
    val couponCount: Int,
)