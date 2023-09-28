package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>{

    fun findByIdAndDeletedIsFalse(productId: Long): Product?
    fun findAllByDeletedIsFalse(pageable: Pageable): Page<Product>
}