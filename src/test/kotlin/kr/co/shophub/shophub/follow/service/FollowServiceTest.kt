package kr.co.shophub.shophub.follow.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kr.co.shophub.shophub.follow.model.Follow
import kr.co.shophub.shophub.follow.repository.FollowRepository
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository

class FollowServiceTest : BehaviorSpec({

    val followRepository = mockk<FollowRepository>()
    val userRepository = mockk<UserRepository>()
    val shopRepository = mockk<ShopRepository>()

    val followService = FollowService(
        followRepository = followRepository,
        userRepository = userRepository,
        shopRepository = shopRepository
    )

    Given("팔로우 로직") {

        val user = User(
            id = 1L,
            email = "test@test.com",
            nickname = "name",
            password = "password",
            phoneNumber = "phoneNumber"
        )

        val shop = Shop(
            id = 2L,
            name = "name",
            latitude = 0.0,
            longitude = 0.0,
            address = "juso",
            telNum = "000",
            introduce = "introduce",
            hour = "hour",
            hourDescription = "description",
            sellerId = 1,
        )

        val followedShop = Shop(
            id = 2L,
            name = "name",
            latitude = 0.0,
            longitude = 0.0,
            address = "juso",
            telNum = "000",
            introduce = "introduce",
            hour = "hour",
            hourDescription = "description",
            sellerId = 1,
            followCnt = 1
        )

        val follow = Follow(
            user = user,
            shop = shop,
        )

        When("팔로우 안한 가게를 팔로우 시") {

            every { userRepository.findByIdAndDeletedIsFalse(any()) } returns user
            every { shopRepository.findByIdAndDeletedIsFalse(any()) } returns shop
            every { followRepository.save(any()) } returns follow
            every { followRepository.existsByShopAndUser(any(), any()) } returns false
            every { followRepository.deleteByShopAndUser(any(), any()) } just Runs

            followService.followShop(user.id, shop.id)

            Then("팔로우에 성공한다") {
                verify(exactly = 1) { followRepository.save(any()) }
            }

            Then("팔로우 수가 오른다") {
                shop.followCnt shouldBe  1
            }
        }

        When("팔로우 한 가게를 팔로우 시") {

            every { userRepository.findByIdAndDeletedIsFalse(any()) } returns user
            every { shopRepository.findByIdAndDeletedIsFalse(any()) } returns followedShop
            every { followRepository.existsByShopAndUser(any(), any()) } returns true
            every { followRepository.deleteByShopAndUser(any(), any()) } just Runs

            followService.followShop(user.id, shop.id)

            Then("언팔로우에 성공한다") {
                verify(exactly = 1) { followRepository.deleteByShopAndUser(any(), any()) }
            }

            Then("팔로우 수가 줄어든다") {
                followedShop.followCnt shouldBe 0
            }
        }

    }
})