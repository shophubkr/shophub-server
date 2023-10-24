package kr.co.shophub.shophub.coupon.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class CreateCouponRequest(
    val content: String,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val startedAt: LocalDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val expiredAt: LocalDate,
) {
    init {
        require(startedAt.isBefore(expiredAt)) {"기간 설정이 잘못 되었습니다."}
    }
}