package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

fun interface ProductTagRepositoryCustom {

    fun searchAllProductTag(
        search : String,
        pageable: Pageable
    ) : Page<Product>
}