package kr.co.shophub.shophub.coupon.dto

import kr.co.shophub.shophub.coupon.model.Coupon
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ShortestExpirationCouponResponse(
    val content: String,
    val dDay: Long,
) {
    constructor(coupon: Coupon, clock: Clock): this(
        content = coupon.content,
        dDay = ChronoUnit.DAYS.between(LocalDate.now(clock), coupon.expiredAt)
    )
}