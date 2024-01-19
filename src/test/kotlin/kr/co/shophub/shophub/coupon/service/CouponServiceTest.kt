package kr.co.shophub.shophub.coupon.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.shophub.shophub.coupon.dto.CouponIdResponse
import kr.co.shophub.shophub.coupon.dto.CreateCouponRequest
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.coupon.repository.CouponRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.dto.CreateShopRequest
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class CouponServiceTest : BehaviorSpec({

    val couponRepository : CouponRepository = mockk()
    val shopRepository : ShopRepository = mockk()
    val clock : Clock = Clock.fixed(Instant.parse("3333-08-22T10:00:00Z"), ZoneOffset.UTC)

    val couponService = CouponService(couponRepository, shopRepository, clock)

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

    val shopId = 1L
    val notShopId = 2L
    val sellerId = 1L
    val userId = 1L
    val shop = Shop(createShopRequest, sellerId).apply { id = shopId }

    val createCouponRequest = CreateCouponRequest(
        content = "Test coupon content",
        detail = "Test coupon detail",
        startedAt = LocalDate.of(2024, 2, 10),
        expiredAt = LocalDate.of(2024, 4, 12)
    )

    val couponId = 1L
    val coupon = Coupon(createCouponRequest, shop).apply { id = couponId }

    Given("가게와 쿠폰 생성 요청이 주어졌을 때"){
        every { couponRepository.save(any()) } returns coupon
        every { shopRepository.findByIdAndDeletedIsFalse(shopId) } returns shop

        When("쿠폰을 생성할 때"){
            val response = couponService.createCoupon(createCouponRequest, sellerId, shopId)

            Then("쿠폰의 ID가 반환된다."){
                response shouldBe CouponIdResponse(couponId)
            }
        }

        When("쿠폰 만료일자가 생성일자보다 더 빠르면"){
            val exception = shouldThrow<IllegalArgumentException> {
                //startedAt = LocalDate.of(2024, 2, 10)
                couponService.createCoupon(createCouponRequest.copy(expiredAt = LocalDate.of(2024, 1, 1)), sellerId, shopId)
            }
            Then("에러가 발생한다."){
                exception.message shouldBe "기간 설정이 잘못 되었습니다."
            }
        }

        When("오늘 일자보다 생성일자가 더 빠르면"){
            val exception = shouldThrow<IllegalArgumentException> {
                //now = 3333-08-22
                couponService.createCoupon(createCouponRequest.copy(startedAt = LocalDate.of(2024, 1, 9)), sellerId, shopId)
            }

            Then("에러가 발생한다."){
                exception.message shouldBe "시작 날짜를 다시 확인해주세요."
            }
        }
    }

    Given("매장 정보가 없을 때의 쿠폰 생성 요청") {
        every { shopRepository.findByIdAndDeletedIsFalse(notShopId) } returns null

        When("쿠폰을 생성하려고 시도할 때") {
            val exception = shouldThrow<ResourceNotFoundException> {
                couponService.createCoupon(createCouponRequest, sellerId, notShopId)
            }

            Then("예외가 발생해야 한다") {
                exception.message shouldBe "매장 정보를 찾을 수 없습니다."
            }
        }
    }

    Given("가게와 쿠폰 생성 요청을 때"){
        val pageable = PageRequest.of(0, 10)
        val isFinished = false
        val expectedPage = PageImpl(listOf(coupon))

        every { couponRepository.findAllByShopIdAndIsTerminatedAndDeletedIsFalse(shopId, isFinished, pageable) } returns expectedPage
        every { shopRepository.existsByIdAndDeletedIsFalse(shopId) } returns true

        When("쿠폰 리스트를 조회할 때"){
            val resultPage = couponService.getCouponList(shopId, isFinished, pageable)

            Then("쿠폰 리스트 페이지가 반환된다."){
                resultPage shouldBe expectedPage
            }
        }
    }

    Given("쿠폰과 사용자 ID가 주어졌을 때") {
        every { couponRepository.findByCouponIdAndDeletedIsFalse(couponId) } returns coupon

        When("terminateCoupon 메소드가 호출될 때") {
            couponService.terminateCoupon(couponId, userId)

            Then("쿠폰은 종료 상태로 변경된다.") {
                coupon.isTerminated shouldBe true
            }
        }
    }

})
