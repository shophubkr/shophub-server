package kr.co.shophub.shophub.coupon.dto

import kr.co.shophub.shophub.coupon.model.Coupon
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class CouponResponse (
    val id: Long,
    val content: String,
    val detail: String,
    val isFinished: Boolean,
    val startedAt: LocalDate,
    val expiredAt: LocalDate,
    val dDay: Long,
){
    constructor(coupon: Coupon, clock: Clock): this(
        id = coupon.id,
        content = coupon.content,
        detail = coupon.detail,
        isFinished = LocalDate.now(clock).isAfter(coupon.expiredAt),
        startedAt = coupon.startedAt,
        expiredAt = coupon.expiredAt,
        dDay = ChronoUnit.DAYS.between(LocalDate.now(clock), coupon.expiredAt)
    )
}