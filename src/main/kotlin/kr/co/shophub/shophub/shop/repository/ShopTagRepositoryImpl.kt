package kr.co.shophub.shophub.shop.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.coupon.model.QCoupon
import kr.co.shophub.shophub.product.model.product.QProduct.*
import kr.co.shophub.shophub.product.model.tag.QProductTag.*
import kr.co.shophub.shophub.search.model.SortBy
import kr.co.shophub.shophub.shop.model.QShop
import kr.co.shophub.shophub.shop.model.QShop.*
import kr.co.shophub.shophub.shop.model.QShopTag.*
import kr.co.shophub.shophub.shop.model.Shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class ShopTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): ShopTagRepositoryCustom {
    override fun searchAllShopWithShopTagAndProductTag(
        search: String,
        hasCoupon: Boolean?,
        sortBy: SortBy?,
        pageable: Pageable
    ): Page<Shop> {

        /**
         *SELECT DISTINCT s.*
         * FROM shop s
         * LEFT JOIN shop_tag st ON s.shop_id = st.shop_id
         * LEFT JOIN product p ON s.shop_id = p.shop_id
         * LEFT JOIN product_tag pt ON p.product_id = pt.product_id
         * LEFT JOIN coupon c ON s.shop_id = c.shop_id
         * WHERE (st.name LIKE '%search%' OR pt.tag LIKE '%search%')
         * AND (c.id IS NOT NULL OR :hasCoupon = false)
         * ORDER BY
         * CASE WHEN :sortBy = 'distance' THEN s.distance END,
         * CASE WHEN :sortBy = 'price' THEN s.price END;
         */

        val query = queryFactory
            .select(shop)
            .from(shop)
            .leftJoin(shop.tags, shopTag).on(shop.id.eq(shopTag.shop.id))
            .leftJoin(shop.products, product).on(shop.id.eq(product.shop.id))
            .leftJoin(product.tags, productTag).on(product.id.eq(productTag.product.id))
            .where(containsSearchShopTag(search)?.or(containsSearchProductTag(search)))

        if (hasCoupon == true) {
            query.where(hasCouponCondition(queryFactory, shop))
        }

        val total = query.fetch().size.toLong()

        val results = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(shop.id.desc())
            .fetch()

        if (sortBy != null) {
            when (sortBy) {
                SortBy.MIN_PRICE -> results.sortBy { it.products.minByOrNull { product -> product.price }?.price ?: 0 }
                else -> results.sortBy { it.id }
            }
        }

        return PageImpl(results, pageable, total)
    }

    private fun containsSearchShopTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { shopTag.name.contains(it) }
    }

    private fun containsSearchProductTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { productTag.tag.contains(it) }
    }

    // shop.coupons에 shop.id가 포함되어 있는지 확인하는 조건 구성
    fun hasCouponCondition(queryFactory: JPAQueryFactory, shopIdPath: QShop): BooleanExpression {
        val qCoupon = QCoupon.coupon
        return queryFactory.selectOne()
            .from(qCoupon)
            .where(qCoupon.shop.id.eq(shopIdPath.id))
            .exists()
    }

}