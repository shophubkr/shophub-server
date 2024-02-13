package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserCoupon
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponRepository : JpaRepository<UserCoupon, Long>, UserCouponCustomRepository{

    @EntityGraph(attributePaths = ["coupon"])
    fun findAllByUser(user: User): List<UserCoupon>
}