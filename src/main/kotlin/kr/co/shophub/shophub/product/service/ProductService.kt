package kr.co.shophub.shophub.product.service

import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.product.dto.CreateProductRequest
import kr.co.shophub.shophub.product.dto.ProductIdResponse
import kr.co.shophub.shophub.product.dto.ProductResponse
import kr.co.shophub.shophub.product.dto.UpdateProductRequest
import kr.co.shophub.shophub.product.model.category.ProductCategory
import kr.co.shophub.shophub.product.model.image.ProductImage
import kr.co.shophub.shophub.product.model.product.Product
import kr.co.shophub.shophub.product.model.tag.ProductTag
import kr.co.shophub.shophub.product.repository.ProductCategoryRepository
import kr.co.shophub.shophub.product.repository.ProductImageRepository
import kr.co.shophub.shophub.product.repository.ProductRepository
import kr.co.shophub.shophub.product.repository.ProductTagRepository
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.shop.service.ShopService
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
        validateShopRequest(createProductRequest.images.size, createProductRequest.tags.size)

        val shop = findShop(shopId)
        val product = Product(createProductRequest, shop)

        val savedProduct = productRepository.save(product)

        saveImage(createProductRequest, savedProduct)
        saveTag(createProductRequest, savedProduct)
        saveCategory(createProductRequest, savedProduct)

        return ProductIdResponse(savedProduct.id)
    }

    private fun saveCategory(
        createProductRequest: CreateProductRequest,
        product: Product
    ) {
        productCategoryRepository.save(
            ProductCategory(
                name = createProductRequest.category,
                product = product,
            )
        )
    }

    private fun saveTag(
        createProductRequest: CreateProductRequest,
        product: Product
    ) {
        val productTags = createProductRequest.tags.map { tag ->
            ProductTag(tag = tag, product = product)
        }
        productTagRepository.saveAll(productTags)
    }

    private fun saveImage(
        createProductRequest: CreateProductRequest,
        savedProduct: Product
    ) {
        val productImages = createProductRequest.images.map { imageUrl ->
            ProductImage(imgUrl = imageUrl, product = savedProduct)
        }
        productImageRepository.saveAll(productImages)
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
        validateShopRequest(updateProductRequest.images.size, updateProductRequest.tags.size)

        val product = findProduct(productId)
        product.updateInfo(updateProductRequest)

        updateImage(product, updateProductRequest)
        updateTag(product, updateProductRequest)
        updateCategory(product, updateProductRequest)

        return ProductIdResponse(productId)
    }

    private fun updateCategory(
        product: Product,
        updateProductRequest: UpdateProductRequest
    ) {
        product.category?.let { category ->
            category.name = updateProductRequest.category
        } ?: run {
            val newCategory = ProductCategory(name = updateProductRequest.category, product = product)
            product.category = newCategory
        }
    }

    private fun updateImage(
        product: Product,
        updateProductRequest: UpdateProductRequest
    ) {
        productImageRepository.deleteAll(product.images)
        product.images.clear()
        val newImages = updateProductRequest.images.map { ProductImage(imgUrl = it, product = product) }
        product.images.addAll(newImages)
    }

    private fun updateTag(
        product: Product,
        updateProductRequest: UpdateProductRequest
    ) {
        productTagRepository.deleteAll(product.tags)
        product.tags.clear()
        val newTags = updateProductRequest.tags.map { ProductTag(tag = it, product = product) }
        product.tags.addAll(newTags)
    }

    @Transactional
    fun softDelete(productId: Long) {
        val product = findProduct(productId)
        product.softDelete()
    }

    private fun validateShopRequest(imageSize: Int, tagSize: Int) {
        require(imageSize >= ProductService.MIN_IMAGE_COUNT) { "이미지 최소 갯수는 ${ProductService.MIN_IMAGE_COUNT} 개 입니다." }
        require(imageSize <= ProductService.MAX_IMAGE_COUNT) { "이미지 최대 갯수는 ${ProductService.MAX_IMAGE_COUNT} 개 입니다." }
        require(tagSize <= ProductService.MAX_TAG_COUNT) { "태그 최대 갯수는 ${ProductService.MAX_TAG_COUNT} 개 입니다." }
    }

    private fun findProduct(productId: Long) = productRepository.findByIdAndDeletedIsFalse(productId)
        ?: throw ResourceNotFoundException("존재하지 않는 상품입니다.")

    companion object {
        private const val MIN_IMAGE_COUNT = 3
        private const val MAX_IMAGE_COUNT = 10
        private const val MAX_TAG_COUNT = 5
    }
}

