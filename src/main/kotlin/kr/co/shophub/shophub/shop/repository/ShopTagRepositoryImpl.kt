package kr.co.shophub.shophub.shop.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.shop.model.QShop.*
import kr.co.shophub.shophub.shop.model.QShopTag.*
import kr.co.shophub.shophub.shop.model.Shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class ShopTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): ShopTagRepositoryCustom {
    override fun searchAllShopTag(
        search: String,
        pageable: Pageable
    ): Page<Shop> {

        val query = queryFactory
            .selectDistinct(shop)
            .from(shopTag)
            .innerJoin(shopTag.shop, shop)
            .where(containsSearchTag(search))

        val total = query.fetch().size.toLong()

        val results = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(shop.id.desc())
            .fetch()

        return PageImpl(results, pageable, total)
    }

    private fun containsSearchTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { shopTag.name.contains(it) }
    }
}