package kr.co.shophub.shophub.coupon.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.coupon.model.QCoupon.coupon
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.model.QShop.shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDate

class CouponRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CouponRepositoryCustom{

    override fun findByExpiredAt(
        shopId: Long,
        isTerminate: Boolean,
        nowDate: LocalDate,
        pageable: Pageable
    ): Page<Coupon> {

        val contents = queryFactory
            .selectFrom(coupon)
            .join(coupon.shop, shop).fetchJoin()
            .where(
                shopIdEq(shopId),
                isFinished(nowDate, isTerminate),
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(coupon.id.desc())
            .fetch()

        val total = queryFactory
            .select(coupon.count())
            .from(coupon)
            .where(
                shopIdEq(shopId),
                isFinished(nowDate, isTerminate),
            )

        return PageableExecutionUtils.getPage(contents, pageable) {
            total.fetchOne() ?: throw ResourceNotFoundException("쿠폰이 존재하지 않습니다.")
        }
    }

    override fun findShortestExpirationCoupons(shopId: Long, nowDate: LocalDate): Coupon {
        return queryFactory
            .selectFrom(coupon)
            .join(coupon.shop, shop).fetchJoin()
            .where(
                shopIdEq(shopId),
                expiredGoe(nowDate),
                )
            .orderBy(coupon.expiredAt.asc())
            .limit(1)
            .fetchOne() ?: throw ResourceNotFoundException("쿠폰이 존재하지 않습니다.")
    }

    private fun isFinished(nowDate: LocalDate, isTerminate: Boolean): BooleanExpression {
        return if (isTerminate) coupon.expiredAt.lt(nowDate)
        else expiredGoe(nowDate)
    }

    private fun expiredGoe(nowDate: LocalDate): BooleanExpression =
        coupon.expiredAt.goe(nowDate)

    private fun shopIdEq(shopId: Long): BooleanExpression =
        coupon.shop.id.eq(shopId)
}