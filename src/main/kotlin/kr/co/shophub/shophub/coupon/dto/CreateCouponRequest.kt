package kr.co.shophub.shophub.coupon.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class CreateCouponRequest(
    val content: String,
    val detail: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startedAt: LocalDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val expiredAt: LocalDate,
) {
}