package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface UserCouponCustomRepository {
    fun findUserCoupons(userId: Long, status: UserCouponCond, pageable: Pageable, nowDate: LocalDate): Page<UserCoupon>
}