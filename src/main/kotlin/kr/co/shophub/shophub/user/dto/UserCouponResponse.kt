package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.user.model.UserCoupon
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class UserCouponResponse(
    val id: Long,
    val couponContent: String,
    val startedAt: LocalDate,
    val expiredAt: LocalDate,
    val shopName: String,
    val dDay: Long,
) {
    constructor(userCoupon: UserCoupon) : this(
        id = userCoupon.id,
        couponContent = userCoupon.coupon.content,
        startedAt = userCoupon.coupon.startedAt,
        expiredAt = userCoupon.coupon.expiredAt,
        shopName = userCoupon.coupon.shop.name,
        dDay = ChronoUnit.DAYS.between(LocalDate.now(), userCoupon.coupon.expiredAt)
    )
}