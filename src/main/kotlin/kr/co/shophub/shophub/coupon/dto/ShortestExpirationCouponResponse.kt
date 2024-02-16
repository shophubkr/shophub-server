package kr.co.shophub.shophub.coupon.dto

import kr.co.shophub.shophub.coupon.model.Coupon
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ShortestExpirationCouponResponse(
    val content: String,
    val dDay: Long,
) {
    constructor(coupon: Coupon, now: LocalDate): this(
        content = coupon.content,
        dDay = ChronoUnit.DAYS.between(now, coupon.expiredAt)
    )
}