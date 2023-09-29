package kr.co.shophub.shophub.product.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.co.shophub.shophub.product.dto.CreateProductRequest
import kr.co.shophub.shophub.product.dto.ProductIdResponse
import kr.co.shophub.shophub.product.dto.UpdateProductRequest
import kr.co.shophub.shophub.product.model.image.ProductImage
import kr.co.shophub.shophub.product.model.product.Product
import kr.co.shophub.shophub.product.model.product.ProductStatus
import kr.co.shophub.shophub.product.model.tag.ProductTag
import kr.co.shophub.shophub.product.repository.ProductCategoryRepository
import kr.co.shophub.shophub.product.repository.ProductImageRepository
import kr.co.shophub.shophub.product.repository.ProductRepository
import kr.co.shophub.shophub.product.repository.ProductTagRepository
import kr.co.shophub.shophub.shop.dto.CreateShopRequest
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository

class ProductServiceTest : BehaviorSpec({
    val productRepository: ProductRepository = mockk()
    val productImageRepository: ProductImageRepository = mockk()
    val productTagRepository: ProductTagRepository = mockk()
    val productCategoryRepository: ProductCategoryRepository = mockk()
    val shopRepository: ShopRepository = mockk()
    val productService = ProductService(
        productRepository,
        productImageRepository,
        productTagRepository,
        productCategoryRepository,
        shopRepository,
        )


    val createShopRequest = CreateShopRequest(
        name = "Test Shop",
        tags = listOf("Tag1", "Tag2"),
        images = listOf("Image1", "Image2", "Image3"),
        address = "Shop Address",
        introduce = "Shop Introduce",
        hour = "Shop Hour",
        hourDescription = "Shop Hour Description",
        telNum = "123-456-7890"
    )

    val shopId = 1L
    val notShopId = 4L
    val sellerId = 1L
    val notSellerId = 3L
    val shop = Shop(createShopRequest, sellerId).apply { id = shopId }

    val createProductRequest = CreateProductRequest(
        name = "Sample Product",
        price = 10000,
        introduce = "This is a sample product for testing.",
        tags = listOf("Tag1", "Tag2"),
        images = listOf("Image1.jpg", "Image2.jpg", "Image3.jpg"),
        status = ProductStatus.SALE,
        category = "Sample Category"
    )

    val productId = 2L
    val product = Product(createProductRequest, shop).apply { id = productId }

    val updateProductRequest = UpdateProductRequest(
        name = "Sample Product",
        price = 12345,
        introduce = "This is a sample product for testing.",
        tags = listOf("Tag1", "Tag2"),
        images = listOf("Image1.jpg", "Image2.jpg", "Image3.jpg"),
        status = ProductStatus.SALE,
        category = "Sample Category"
    )

    val loginUserId = 1L

    Given("가게와 상품 생성 요청이 주어졌을 때") {
        every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop
        every { productRepository.save(any()) } returns product
        every { productImageRepository.saveAll(any<List<ProductImage>>()) } returns listOf()
        every { productTagRepository.saveAll(any<List<ProductTag>>()) } returns listOf()
        every { productCategoryRepository.save(any()) } returns mockk()


        When("상품을 생성하려고 할 때") {
            val response = productService.createProduct(loginUserId, shopId, createProductRequest)

            Then("새로운 상품 ID가 반환되어야 한다.") {
                response shouldBe ProductIdResponse(productId)
                verify(exactly = 1) { productRepository.save(any()) }
                verify(exactly = 1) { productImageRepository.saveAll(any<List<ProductImage>>()) }
                verify(exactly = 1) { productTagRepository.saveAll(any<List<ProductTag>>()) }
                verify(exactly = 1) { productCategoryRepository.save(any()) }
            }
        }

        When("이미지 개수가 3개 미만일 때"){
            val invalidRequest = createProductRequest.copy(
                images = listOf("Image1.jpg", "Image2.jpg")
            )
            val exception = shouldThrow<IllegalArgumentException> {
                productService.createProduct(loginUserId, shopId, invalidRequest,)
            }

            Then("예외가 발생해야 한다") {
                exception.message shouldBe "이미지 최소 갯수는 3 개 입니다."
            }
        }

        When("이미지 개수가 10개를 넘을 때"){
            val list: MutableList<String> = MutableList(11) { "image $it" }

            val invalidRequest = createProductRequest.copy(
                images = list
            )
            val exception = shouldThrow<IllegalArgumentException> {
                productService.createProduct(loginUserId, shopId, invalidRequest,)
            }

            Then("예외가 발생해야 한다") {
                exception.message shouldBe "이미지 최대 갯수는 10 개 입니다."
            }
        }

        When("태그 개수가 5개를 넘을 때"){
            val list: MutableList<String> = MutableList(6) { "item $it" }

            val invalidRequest = createProductRequest.copy(
                tags = list
            )
            val exception = shouldThrow<IllegalArgumentException> {
                productService.createProduct(loginUserId, shopId, invalidRequest,)
            }

            Then("예외가 발생해야 한다") {
                exception.message shouldBe "태그 최대 갯수는 5 개 입니다."
            }
        }
    }

    Given("매장 정보가 없을 때의 상품 생성 요청") {
        every { shopRepository.findByIdAndDeletedIsFalse(notShopId) } returns null

        When("상품을 생성하려고 시도할 때") {
            val exception = shouldThrow<IllegalArgumentException> {
                productService.createProduct(loginUserId, notShopId, createProductRequest,)
            }

            Then("예외가 발생해야 한다") {
                exception.message shouldBe "매장 정보를 찾을 수 없습니다."
            }
        }
    }

    Given("기존의 제품과 업데이트 요청이 있을 때") {
        every { productRepository.findByIdAndDeletedIsFalse(productId) } returns product
        every { productImageRepository.deleteAll(any<List<ProductImage>>()) } returns Unit
        every { productTagRepository.deleteAll(any<List<ProductTag>>()) } returns Unit

        When("제품을 업데이트하면") {
            val response = productService.updateProduct(productId, updateProductRequest, loginUserId)

            Then("동일한 제품 ID가 반환된다") {
                response shouldBe ProductIdResponse(productId)
                verify { productImageRepository.deleteAll(product.images) }
                verify { productTagRepository.deleteAll(product.tags) }
            }
        }
    }

    Given("삭제될 기존의 제품 ID일 때") {
        every { productRepository.findByIdAndDeletedIsFalse(productId) } returns product

        When("제품을 삭제하면") {
            productService.softDelete(productId, loginUserId)

            Then("제품이 삭제로 표시된다") {
                verify { productRepository.findByIdAndDeletedIsFalse(productId) }
            }
        }
    }

})
