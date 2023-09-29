package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long>{


    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.images " +
            "JOIN FETCH p.category " +
            "WHERE p.id = :productId AND p.deleted = false")
    fun findByIdAndDeletedIsFalse(productId: Long): Product?
    fun findAllByDeletedIsFalse(pageable: Pageable): Page<Product>
}