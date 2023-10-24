package kr.co.shophub.shophub.shop.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.product.model.product.QProduct.*
import kr.co.shophub.shophub.product.model.tag.QProductTag.*
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
        pageable: Pageable
    ): Page<Shop> {

        /**
         * SELECT DISTINCT s.*
         * FROM shop s
         * LEFT JOIN shop_tag st ON s.shop_id = st.shop_id
         * LEFT JOIN product p ON s.shop_id = p.shop_id
         * LEFT JOIN product_tag pt ON p.product_id = pt.product_id
         * WHERE st.name LIKE '%search%' OR pt.tag LIKE '%search%';
         */

        val query = queryFactory
            .select(shop)
            .from(shop)
            .leftJoin(shop.tags, shopTag).on(shop.id.eq(shopTag.shop.id))
            .leftJoin(shop.products, product).on(shop.id.eq(product.shop.id))
            .leftJoin(product.tags, productTag).on(product.id.eq(productTag.product.id))
            .where(containsSearchShopTag(search)?.or(containsSearchProductTag(search)))

        val total = query.fetch().size.toLong()

        val results = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(shop.id.desc())
            .fetch()

        return PageImpl(results, pageable, total)
    }

    private fun containsSearchShopTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { shopTag.name.contains(it) }
    }

    private fun containsSearchProductTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { productTag.tag.contains(it) }
    }
}