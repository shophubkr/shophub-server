package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserCouponCustomRepository {
    fun findUserCoupons(userId: Long, status: UserCouponCond, pageable: Pageable): Page<UserCoupon>
}