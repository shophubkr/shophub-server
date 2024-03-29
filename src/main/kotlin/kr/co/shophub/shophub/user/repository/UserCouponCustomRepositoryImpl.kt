package kr.co.shophub.shophub.user.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.coupon.model.QCoupon.coupon
import kr.co.shophub.shophub.shop.model.QShop.shop
import kr.co.shophub.shophub.user.model.QUserCoupon.userCoupon
import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class UserCouponCustomRepositoryImpl (
    private val queryFactory: JPAQueryFactory
): UserCouponCustomRepository{

    override fun findUserCoupons(
        userId: Long,
        status: UserCouponCond,
        pageable: Pageable,
        nowDate: LocalDate,
    ): Page<UserCoupon> {
        val contents = queryFactory.selectFrom(userCoupon)
            .join(userCoupon.coupon, coupon).fetchJoin()
            .join(coupon.shop, shop).fetchJoin()
            .where(
                userCoupon.user.id.eq(userId).and(
                    couponSearchCond(status, nowDate)
                )
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(userCoupon.createdAt.desc())
            .fetch()
        pageable.sort

        return PageImpl(contents)
    }
    private fun couponSearchCond(status: UserCouponCond, nowDate: LocalDate): BooleanExpression =
        when (status) {
            UserCouponCond.USED -> userCoupon.isUsed.isTrue
            UserCouponCond.UNUSED -> userCoupon.isUsed.isFalse.and(coupon.expiredAt.before(nowDate).or(coupon.expiredAt.eq(nowDate)))
            UserCouponCond.EXPIRED -> userCoupon.isUsed.isFalse.and(coupon.expiredAt.after(nowDate))
        }
}

