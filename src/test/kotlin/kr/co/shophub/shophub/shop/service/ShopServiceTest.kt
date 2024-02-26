package kr.co.shophub.shophub.shop.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kr.co.shophub.shophub.business.model.Business
import kr.co.shophub.shophub.business.repository.BusinessRepository
import kr.co.shophub.shophub.geo.client.KakaoLocalClient
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.dto.*
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.model.ShopImage
import kr.co.shophub.shophub.shop.model.ShopTag
import kr.co.shophub.shophub.shop.repository.ShopImageRepository
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.shop.repository.ShopTagRepository
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository

class ShopServiceTest : BehaviorSpec({
    val shopRepository = mockk<ShopRepository>()
    val shopImageRepository = mockk<ShopImageRepository>()
    val shopTagRepository = mockk<ShopTagRepository>()
    val userRepository = mockk<UserRepository>()
    val businessRepository = mockk<BusinessRepository>()
    val kakaoLocalClient = mockk<KakaoLocalClient>()
    val shopService = ShopService(
        shopRepository = shopRepository,
        shopImageRepository = shopImageRepository,
        shopTagRepository = shopTagRepository,
        userRepository = userRepository,
        businessRepository = businessRepository,
        kakaoLocalClient = kakaoLocalClient,
        restApiKey = "restApiKey"
    )

    val sellerId = 1L
    val notSellerId = 3L
    val createShopRequest = CreateShopRequest(
        name = "Test Shop",
        tags = listOf("Tag1", "Tag2"),
        images = listOf("Image1", "Image2", "Image3"),
        address = "Shop Address",
        introduce = "Shop Introduce",
        hour = "Shop Hour",
        hourDescription = "Shop Hour Description",
        telNum = "123-456-7890",
        businessNumber = "bizNum"
    )

    val shopId = 2L
    val notShopId = 4L
    val latitude = 0.0
    val longitude = 0.0
    val shop = Shop(createShopRequest, latitude, longitude, sellerId).apply { id = shopId }
    val changeShopRequest = ChangeShopRequest(
        name = "Changed Shop",
        tags = listOf("Tag3", "Tag4"),
        images = listOf("Image4", "Image5", "Image6"),
        address = "Changed Address",
        introduce = "Changed Introduce",
        hour = "Changed Hour",
        hourDescription = "Changed Hour Description",
        telNum = "987-654-3210"
    )

    val seller = User(
        email = "email",
        password = "password",
        nickname = "nickname",
        phoneNumber = "telNum"
    )

    val business = Business(
        businessNumber = "bizNum",
        sellerId = seller.id,
        shopId = shop.id,
    )

    beforeTest {
        clearMocks(shopRepository, shopImageRepository, shopTagRepository)
    }

    /*Given("createShop 메서드") {

        When("정상적인 요청이 들어올 때") {
            every { shopRepository.findAllByNameAndTelNumAndDeletedIsFalse(any(), any()) } returns emptyList()
            every { shopRepository.save(any()) } returns shop
            every { shopImageRepository.saveAll(any<List<ShopImage>>()) } returns listOf(mockk())
            every { shopTagRepository.saveAll(any<List<ShopTag>>()) } returns listOf(mockk())
            every { userRepository.findByIdAndDeletedIsFalse(sellerId) } returns seller
            every { businessRepository.save(any()) } returns business
            every { businessRepository.existsByBusinessNumber(any()) } returns false

            val response = shopService.createShop(sellerId, createShopRequest)

            Then("ShopIdResponse를 반환해야 함") {
                response shouldBe ShopIdResponse(shopId)
            }
        }


        When("이미지나 태그 갯수가 잘못된 경우") {
            val invalidRequest = createShopRequest.copy(
                images = emptyList(),
                tags = listOf("Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6")
            )
            val exception = shouldThrow<IllegalArgumentException> {
                shopService.createShop(sellerId, invalidRequest)
            }

            Then("예외가 발생해야 함") {

                exception.message shouldBe "이미지 최소 갯수는 3 개 입니다."
            }
        }

        When("이미 존재 하는 사업체인 경우") {

            every { businessRepository.existsByBusinessNumber(any()) } returns true

            val exception = shouldThrow<IllegalArgumentException> {
                shopService.createShop(sellerId, createShopRequest)
                shopService.createShop(sellerId, createShopRequest)
            }

            Then("예외가 발생해야 함") {

                exception.message shouldBe "이미 존재하는 사업체 입니다."
            }
        }
    }*/

    Given("getShop 메서드") {
        When("존재하는 샵 ID로 요청이 들어올 때") {
            every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop

            val response = shopService.getShop(shopId)

            Then("ShopResponse를 반환해야 함") {
                response shouldBe ShopResponse(shop)
            }
        }

        When("존재하지 않는 샵 ID로 요청이 들어올 때") {
            every { shopRepository.findByIdAndDeletedIsFalse(notShopId) } returns null

            val exception = shouldThrow<ResourceNotFoundException> {
                shopService.getShop(notShopId)
            }

            Then("예외가 발생해야 함") {
                exception.message shouldBe "존재하지 않는 상점입니다."
            }
        }
    }

    /*Given("changeShop 메서드") {
        When("정상적인 요청이 들어올 때") {
            every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop
            every { shopRepository.findAllByNameAndTelNumAndDeletedIsFalse(any(), any()) } returns emptyList()
            every { shopImageRepository.saveAll(any<List<ShopImage>>()) } returns listOf(mockk())
            every { shopTagRepository.saveAll(any<List<ShopTag>>()) } returns listOf(mockk())

            val response = shopService.changeShop(shopId, sellerId, changeShopRequest)

            Then("ShopIdResponse를 반환해야 함") {
                response shouldBe ShopIdResponse(shopId)
            }
        }

        When("변경 권한이 없는 경우") {
            every { shopRepository.findAllByNameAndTelNumAndDeletedIsFalse(any(), any()) } returns emptyList()
            every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop

            val exception = shouldThrow<IllegalStateException> {
                shopService.changeShop(shopId, notSellerId, changeShopRequest)
            }

            Then("예외가 발생해야 함") {
                exception.message shouldBe "변경 권한이 없습니다."
            }
        }

    }*/

    Given("deleteShop 메서드") {
        When("삭제 권한이 있는 경우") {
            every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop

            val response = shopService.deleteShop(shopId, sellerId)

            Then("예외가 발생하지 않아야 함") {
                response shouldBe Unit
            }
        }

        When("삭제 권한이 없는 경우") {
            every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop
            val exception = shouldThrow<IllegalStateException> {
                shopService.deleteShop(shopId, notSellerId)
            }

            Then("예외가 발생해야 함") {
                exception.message shouldBe "삭제 권한이 없습니다."
            }
        }
    }
})
