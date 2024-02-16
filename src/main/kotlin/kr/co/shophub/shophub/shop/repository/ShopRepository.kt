package kr.co.shophub.shophub.shop.repository

import kr.co.shophub.shophub.shop.model.Shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ShopRepository : JpaRepository<Shop, Long> {
    fun findAllByNameAndTelNumAndDeletedIsFalse(name: String, telNum: String): List<Shop>
    fun findByIdAndDeletedIsFalse(id: Long): Shop?
    fun existsByIdAndDeletedIsFalse(id: Long): Boolean
    fun findAllByDeletedIsFalse(pageable: Pageable): Page<Shop>
    fun existsByNameAndDeletedIsFalse(name: String): Boolean

    @EntityGraph(attributePaths = ["products"])
    fun findAllBySellerId(sellerId: Long): List<Shop>
}