package kr.co.shophub.shophub.coupon.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.coupon.model.QCoupon.coupon
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.model.QShop.shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class CouponRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CouponRepositoryCustom{

    override fun findByExpiredAt(shopId: Long, isTerminate: Boolean, pageable: Pageable): Page<Coupon> {

        val contents = queryFactory
            .selectFrom(coupon)
            .join(coupon.shop, shop).fetchJoin()
            .where(
                shopIdEq(shopId),
                isTerminateEq(isTerminate),
                isNotDeleted()
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(coupon.id.desc())
            .fetch()

        val total = queryFactory
            .select(coupon.count())
            .from(coupon)
            .leftJoin(coupon.shop, shop)
            .where(
                shopIdEq(shopId),
                isTerminateEq(isTerminate)
            )
            .fetchOne() ?: throw ResourceNotFoundException("")


        return PageImpl(contents, pageable, total)
    }

    private fun isNotDeleted(): BooleanExpression {
        return coupon.deleted.isFalse
    }

    private fun shopIdEq(shopId: Long): BooleanExpression =
        coupon.shop.id.eq(shopId)

    private fun isTerminateEq(isTerminate: Boolean): BooleanExpression =
        coupon.isTerminated.eq(isTerminate)

}