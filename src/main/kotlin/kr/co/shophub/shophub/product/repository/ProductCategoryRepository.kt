package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.category.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ProductCategoryRepository : JpaRepository<ProductCategory, Long> {
}