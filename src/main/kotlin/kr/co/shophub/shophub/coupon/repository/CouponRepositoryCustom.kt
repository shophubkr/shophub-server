package kr.co.shophub.shophub.coupon.repository

import kr.co.shophub.shophub.coupon.model.Coupon
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CouponRepositoryCustom {
    fun findByExpiredAt(shopId : Long, isTerminate : Boolean, pageable: Pageable) : Page<Coupon>
}