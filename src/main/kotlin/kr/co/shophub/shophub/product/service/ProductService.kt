package kr.co.shophub.shophub.product.service

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.product.dto.*
import kr.co.shophub.shophub.product.model.product.Product
import kr.co.shophub.shophub.product.repository.ProductCategoryRepository
import kr.co.shophub.shophub.product.repository.ProductImageRepository
import kr.co.shophub.shophub.product.repository.ProductRepository
import kr.co.shophub.shophub.product.repository.ProductTagRepository
import kr.co.shophub.shophub.shop.repository.ShopRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val productTagRepository: ProductTagRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val shopRepository: ShopRepository,
    ) {

    fun createProduct(shopId: Long, createProductRequest: CreateProductRequest): ProductIdResponse {
        TODO("Not yet implemented")
    }

    fun getProduct(productId: Long): ProductResponse {
        TODO("Not yet implemented")
    }

    fun getProductList(pageable: Pageable): Page<Product> {
        TODO("Not yet implemented")
    }

    fun changeProduct(productId: Long, updateProductRequest: UpdateProductRequest): ProductIdResponse {
        TODO("Not yet implemented")
    }

    fun deleteProduct(productId: String) {
        TODO("Not yet implemented")
    }
}