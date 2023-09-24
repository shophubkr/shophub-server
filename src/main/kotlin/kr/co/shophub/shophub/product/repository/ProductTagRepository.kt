package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.tag.ProductTag
import org.springframework.data.jpa.repository.JpaRepository

interface ProductTagRepository : JpaRepository<ProductTag, Long> {
}