package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond

interface UserCouponCustomRepository {
    fun findUserCoupons(userId: Long, status: UserCouponCond): List<UserCoupon>
}