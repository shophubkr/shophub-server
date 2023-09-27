package kr.co.shophub.shophub.product.service

import kr.co.shophub.shophub.product.dto.*
import kr.co.shophub.shophub.product.model.category.ProductCategory
import kr.co.shophub.shophub.product.model.image.ProductImage
import kr.co.shophub.shophub.product.model.product.Product
import kr.co.shophub.shophub.product.model.tag.ProductTag
import kr.co.shophub.shophub.product.repository.ProductCategoryRepository
import kr.co.shophub.shophub.product.repository.ProductImageRepository
import kr.co.shophub.shophub.product.repository.ProductRepository
import kr.co.shophub.shophub.product.repository.ProductTagRepository
import kr.co.shophub.shophub.shop.repository.ShopRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val productTagRepository: ProductTagRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val shopRepository: ShopRepository,
    ) {

    @Transactional
    fun createProduct(shopId: Long, createProductRequest: CreateProductRequest): ProductIdResponse {
        val shop = findShop(shopId)
        val product = Product(createProductRequest, shop)

        val savedProduct = productRepository.save(product)

        val productImages = createProductRequest.images.map { imageUrl ->
            ProductImage(imgUrl = imageUrl, product = savedProduct)
        }
        productImageRepository.saveAll(productImages)

        val productTags = createProductRequest.tags.map { tag ->
            ProductTag(tag = tag, product = product)
        }
        productTagRepository.saveAll(productTags)

        productCategoryRepository.save(ProductCategory(
            name = createProductRequest.category,
            product = product,
        ))
        return ProductIdResponse(savedProduct.id)
    }

    private fun findShop(shopId: Long) = (shopRepository.findByIdAndDeletedIsFalse(shopId)
        ?: throw IllegalArgumentException("상품에 대한 매장 정보를 찾을 수 없습니다."))

    @Transactional(readOnly = true)
    fun getProduct(productId: Long): ProductResponse {
        val product = findProduct(productId)
        return ProductResponse(product)
    }

    @Transactional(readOnly = true)
    fun getProductList(pageable: Pageable): Page<Product> {
        return productRepository.findAllByDeletedIsFalse(pageable)
    }

    @Transactional
    fun updateProduct(productId: Long, updateProductRequest: UpdateProductRequest): ProductIdResponse {
        val product = findProduct(productId)
        product.updateInfo(updateProductRequest)

        productImageRepository.deleteAll(product.images)
        productTagRepository.deleteAll(product.tags)

        product.images.clear()
        product.tags.clear()

        val newImages = updateProductRequest.images.map { ProductImage(imgUrl = it, product = product) }
        val newTags = updateProductRequest.tags.map { ProductTag(tag = it, product = product) }

        product.images.addAll(newImages)
        product.tags.addAll(newTags)

        productRepository.save(product)

        return ProductIdResponse(productId)
    }

    @Transactional
    fun softDelete(productId: Long) {
        val product = findProduct(productId)
        product.softDelete()
    }

    private fun findProduct(productId: Long) = productRepository.findByIdAndDeletedIsFalse(productId)
}

