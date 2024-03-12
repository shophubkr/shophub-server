package kr.co.shophub.shophub.shop.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.coupon.model.QCoupon
import kr.co.shophub.shophub.product.model.product.QProduct.product
import kr.co.shophub.shophub.product.model.tag.QProductTag.productTag
import kr.co.shophub.shophub.search.model.SortBy
import kr.co.shophub.shophub.shop.model.QShop.shop
import kr.co.shophub.shophub.shop.model.QShopTag.shopTag
import kr.co.shophub.shophub.shop.model.Shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

/**
 * SELECT s.*, COALESCE(MIN(p.price), 0) AS lowest_price
 * FROM shop s
 * LEFT JOIN shop_tag st ON s.shop_id = st.shop_id
 * LEFT JOIN product p ON s.shop_id = p.shop_id
 * LEFT JOIN product_tag pt ON p.product_id = pt.product_id
 * WHERE (st.name LIKE '%태그4%' OR pt.tag LIKE '%태그4%')
 * GROUP BY s.shop_id
 * ORDER BY lowest_price;
 */
class ShopTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): ShopTagRepositoryCustom {
    override fun searchAllShopWithShopTagAndProductTag(
        search: String,
        hasCoupon: Boolean?,
        sortBy: SortBy?,
        pageable: Pageable
    ): Page<Shop> {
        val query = queryFactory
            .selectDistinct(shop)
            .from(shop)
            .leftJoin(shop.tags, shopTag).on(shop.id.eq(shopTag.shop.id))
            .leftJoin(shop.products, product).on(shop.id.eq(product.shop.id))
            .leftJoin(product.tags, productTag).on(product.id.eq(productTag.product.id))
            .where(containsSearchShopTag(search)?.or(containsSearchProductTag(search)))

        if (sortBy == SortBy.MIN_PRICE) {
            query.groupBy(shop.id)
                .orderBy(product.price.min().coalesce(0).asc())
        } else {
            // 기본 정렬
            query.orderBy(shop.id.desc())
        }

        if (hasCoupon != null) {
            val qCoupon = QCoupon.coupon
            val couponExists = queryFactory.selectOne()
                .from(qCoupon)
                .where(
                    qCoupon.shop.id.eq(shop.id),
                    qCoupon.isTerminated.eq(false)
                )
            query.where(
                if (hasCoupon) couponExists.exists()
                else couponExists.notExists()
            )
        }

        val results = query
            .orderBy(shop.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = query.fetch().size.toLong()

        return PageImpl(results, pageable, total)
    }

    private fun containsSearchShopTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { shopTag.name.contains(it) }
    }

    private fun containsSearchProductTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { productTag.tag.contains(it) }
    }
}