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
import kotlin.jvm.optionals.getOrNull

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
        )

        val shop = Shop(
            id = 2L,
            name = "name",
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

        val followList = mutableListOf<Follow>()

        When("팔로우 안한 가게를 팔로우 시") {

            every { userRepository.findById(any()).getOrNull() } returns user
            every { shopRepository.findById(any()).getOrNull() } returns shop
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

            every { userRepository.findById(any()).getOrNull() } returns user
            every { shopRepository.findById(any()).getOrNull() } returns followedShop
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

        When("팔로우한 매장 조회시") {

            every { userRepository.findById(any()).getOrNull() } returns user
            every { followRepository.findByUser(any()) } returns followList

            followList.add(follow)
            val allFollowShop = followService.getAllFollowShop(user.id)

            Then("모든 팔로우 매장일 조회된다.") {
                allFollowShop.size shouldBe 1
                allFollowShop[0].id shouldBe shop.id
            }

        }

    }
})