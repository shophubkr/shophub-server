package kr.co.shophub.shophub.coupon.repository

import kr.co.shophub.shophub.coupon.model.Coupon
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface CouponRepositoryCustom {
    fun findByExpiredAt(shopId: Long, isTerminate: Boolean, nowDate: LocalDate, pageable: Pageable) : Page<Coupon>
    fun findShortestExpirationCoupons(shopId: Long, nowDate: LocalDate) : Coupon
}