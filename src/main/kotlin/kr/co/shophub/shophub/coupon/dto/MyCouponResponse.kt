package kr.co.shophub.shophub.coupon.dto

data class MyCouponResponse(
    val myCoupons: List<CouponResponse>,
    val total: Int,
    val progress: Int,
    val finish: Int,
)