package kr.co.shophub.shophub.product.repository

import kr.co.shophub.shophub.product.model.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>{
}