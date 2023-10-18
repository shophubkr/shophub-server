package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.user.model.UserCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponRepository : JpaRepository<UserCoupon, Long>, UserCouponCustomRepository{
}