package kr.co.shophub.shophub.coupon.repository

import kr.co.shophub.shophub.coupon.model.Coupon
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CouponRepository : JpaRepository<Coupon, Long>, CouponRepositoryCustom{

    @Query("""
        SELECT c
        FROM Coupon c
        JOIN FETCH c.shop
        WHERE c.id = :couponId AND c.deleted = false
    """)
    fun findByCouponIdAndDeletedIsFalse(couponId: Long): Coupon?

    @Modifying
    @Query("""
        UPDATE Coupon c 
        SET c.isTerminated = true
        WHERE NOW() > c.expiredAt
    """)
    fun updateCouponByExpiredAt()
}