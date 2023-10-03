package kr.co.shophub.shophub.user.serivce

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.dto.InfoUpdateRequest
import kr.co.shophub.shophub.user.dto.PasswordRequest
import kr.co.shophub.shophub.user.dto.PasswordUpdateRequest
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository
import kr.co.shophub.shophub.user.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.jvm.optionals.getOrNull

class UserServiceTest: BehaviorSpec({
    val userRepository = mockk<UserRepository>()
    val shopRepository = mockk<ShopRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()

    val userService = UserService(
        userRepository = userRepository,
        shopRepository = shopRepository,
        passwordEncoder = passwordEncoder
    )

    Given("로그인 상태에서") {

        val userId = 1L

        val user = User(
            id = userId,
            email = "test@test.com",
            password = "password",
            nickname = "name",
        )

        val shopList = mutableListOf<Shop>()

        val infoUpdateRequest = InfoUpdateRequest(
            nickname = "name",
            newPassword = "newPassword"
        )

        val passwordRequest = PasswordRequest(
            password = "password"
        )

        val passwordUpdateRequest = PasswordUpdateRequest(
            email = "test@test.com",
            newPassword = "newPassword"
        )

        When("마이페이지 접근시") {

            every { userRepository.findById(userId).getOrNull() } returns user
            every { shopRepository.findAll() } returns shopList

            val myPageResponse = userService.getMyPage(userId)

            Then("정보를 내어 준다.") {
                myPageResponse.email shouldBe user.email
                myPageResponse.followShop shouldNotBe null
                myPageResponse.coupon shouldNotBe null
            }
        }

        When("정보 수정 요청시") {

            every { userRepository.findById(userId).getOrNull() } returns user
            every { passwordEncoder.encode(any()) } returns "newPassword"

            userService.updateInfo(userId, infoUpdateRequest)

            Then("업데이트 반영 ") {
                user.nickname shouldBe infoUpdateRequest.nickname
                user.password shouldBe infoUpdateRequest.newPassword
            }
        }

        When("올바른 비밀번호를 확인시") {

            every { passwordEncoder.matches(any(), any()) } returns true

            val checkResponse = userService.checkPassword(passwordRequest, userId)

            Then("비밀번호 확인 성공") {
                checkResponse.result shouldBe "비밀번호가 확인 되었습니다!"
            }
        }

        When("잘못된 비밀번호를 확인시") {

            every { passwordEncoder.matches(any(), any()) } returns false

            val checkResponse = userService.checkPassword(passwordRequest, userId)

            Then("비밀번호 확인 성공") {
                checkResponse.result shouldBe "비밀번호가 일치하지 않습니다!"
            }
        }

        When("비밀번호 변경 요청시") {

            every { userRepository.findByEmail(passwordUpdateRequest.email) } returns user
            every { passwordEncoder.encode(any()) } returns "newPassword"

            userService.updatePassword(passwordUpdateRequest)

            Then("비밀번호 변경 성공") {
                user.password shouldBe passwordUpdateRequest.newPassword
            }
        }
    }
})