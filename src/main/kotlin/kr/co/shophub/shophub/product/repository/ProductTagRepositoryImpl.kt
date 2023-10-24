package kr.co.shophub.shophub.product.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.shophub.shophub.product.model.product.Product
import kr.co.shophub.shophub.product.model.product.QProduct.*
import kr.co.shophub.shophub.product.model.tag.QProductTag.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class ProductTagRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): ProductTagRepositoryCustom {

    override fun searchAllProductTag(
        search: String,
        pageable: Pageable
    ): Page<Product> {

        val query = queryFactory
            .selectDistinct(product)
            .from(productTag)
            .innerJoin(productTag.product, product)
            .where(containsSearchTag(search))

        val total = query.fetch().size.toLong()
        
        val results = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(product.id.desc())
            .fetch()

        return PageImpl(results, pageable, total)
    }

    private fun containsSearchTag(search: String?): BooleanExpression? {
        return search?.takeIf { it.isNotBlank() }?.let { productTag.tag.contains(it) }
    }
}