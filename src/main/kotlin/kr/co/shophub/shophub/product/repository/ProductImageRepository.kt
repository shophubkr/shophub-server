package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.image.ProductImage
import org.springframework.data.jpa.repository.JpaRepository

interface ProductImageRepository : JpaRepository<ProductImage, Long> {
}