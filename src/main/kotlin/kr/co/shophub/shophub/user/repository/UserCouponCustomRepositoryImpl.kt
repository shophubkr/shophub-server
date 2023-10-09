package kr.co.shophub.shophub.user.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.coupon.model.QCoupon.coupon
import kr.co.shophub.shophub.user.model.QUserCoupon.userCoupon
import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class UserCouponCustomRepositoryImpl (
    private val queryFactory: JPAQueryFactory
): UserCouponCustomRepository{

    override fun findUserCoupons(
        userId: Long,
        status: UserCouponCond
    ): List<UserCoupon>{
        return queryFactory.selectFrom(userCoupon)
            .join(userCoupon.coupon, coupon)
            .where(
                userCoupon.user.id.eq(userId).and(
                    couponSearchCond(status)
                )
            ).fetch()
    }
    private fun couponSearchCond(status: UserCouponCond): BooleanExpression =
        when (status) {
            UserCouponCond.USED -> userCoupon.isUsed.isTrue
            UserCouponCond.UNUSED -> userCoupon.isUsed.isFalse
            UserCouponCond.EXPIRED -> coupon.expiredAt.before(LocalDate.now())
        }
}

