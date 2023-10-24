package kr.co.shophub.shophub.user.serivce

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.dto.JoinRequest
import kr.co.shophub.shophub.user.dto.LoginRequest
import kr.co.shophub.shophub.user.dto.TokenResponse
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserRole
import kr.co.shophub.shophub.user.repository.UserRepository
import kr.co.shophub.shophub.user.service.AuthService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.lang.RuntimeException

class AuthServiceTest : BehaviorSpec({

    val userRepository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val jwtService = mockk<JwtService>()
    val authenticationManager = mockk<AuthenticationManager>()
    val publisher = mockk<ApplicationEventPublisher>(relaxed = true)

    val authService = AuthService(
        userRepository,
        passwordEncoder,
        jwtService,
        authenticationManager,
        publisher
    )

    Given("회원 가입 테스트") {

        val request = JoinRequest(
            "test@test.com",
            "password",
            "name",
            UserRole.GUEST_BUYER
        )

        val user = User(
            id = 1L,
            email = "test@test.com",
            password = "password",
            nickname = "name",
            phoneNumber = "telNum",
        )

        When("정상 회원 가입 시도") {

            every { userRepository.existsByEmail(any()) } returns false
            every { userRepository.existsByNickname(any()) } returns false
            every { userRepository.save(any()) } returns user
            every { passwordEncoder.encode(request.password) } returns "encodedPassword"

            Then("회원 가입 성공") {
                val response = authService.join(request)
                response.id shouldNotBe null
            }
        }

        When("중복 이메일 시도") {

            every { userRepository.existsByEmail(any()) } returns true
            every { userRepository.existsByNickname(any()) } returns false
            every { userRepository.save(any()) } returns user
            every { passwordEncoder.encode(request.password) } returns "encodedPassword"

            Then("예외 발생") {
                val message = shouldThrow<IllegalStateException> {
                    authService.join(request)
                }.message
                message shouldBe "이미 가입한 이메일 입니다."
            }
        }

        When("중복 닉네임 시도") {

            every { userRepository.existsByEmail(any()) } returns false
            every { userRepository.existsByNickname(any()) } returns true
            every { userRepository.save(any()) } returns user
            every { passwordEncoder.encode(request.password) } returns "encodedPassword"

            Then("예외 발생") {
                val message = shouldThrow<IllegalStateException> {
                    authService.join(request)
                }.message
                message shouldBe "이미 가입한 닉네임 입니다."
            }
        }
    }

    Given("로그인 테스트") {

        val request = LoginRequest(
            "email",
            "password"
        )

        val authentication = UsernamePasswordAuthenticationToken(
            request.email,
            request.password
        )

        val tokenResponse = TokenResponse(
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )

        When("정상 로그인 시도") {

            every { authenticationManager.authenticate(any()) } returns authentication
            every { jwtService.makeTokenResponse(authentication.name) } returns tokenResponse

            Then("로그인 성공") {
                val response = authService.login(authentication)
                response.accessToken shouldBe "accessToken"
                response.refreshToken shouldBe "refreshToken"
            }
        }

        When("비정상 로그인 시도") {

            every { authenticationManager.authenticate(any()) } throws RuntimeException("인증 오류")

            Then("로그인 실패") {
                val message = shouldThrow<RuntimeException> {
                    authService.login(authentication)
                }.message
                message shouldBe "인증 오류"
            }
        }

    }

    Given("토큰 재발행") {

        val refreshToken = "refreshToken"

        val user = User(
            id = 1L,
            email = "test@test.com",
            password = "password",
            nickname = "name",
            phoneNumber = "telNum",
        )

        val tokenResponse = TokenResponse(
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )

        When("토큰이 유효할 때") {

            every { jwtService.isTokenValid(any()) } returns true
            every { userRepository.findByRefreshToken(refreshToken) } returns user
            every { jwtService.makeTokenResponse(user.email) } returns tokenResponse

            Then("재발행 진행") {
                val response = authService.reIssueToken(refreshToken)
                response.accessToken shouldBe "accessToken"
                response.refreshToken shouldBe "refreshToken"
            }
        }

        When("토큰이 유효하지 않을 때") {

            every { jwtService.isTokenValid(any()) } returns false
            every { userRepository.findByRefreshToken(refreshToken) } returns user
            every { jwtService.makeTokenResponse(user.email) } returns tokenResponse

            Then("재발행 살패") {
                val message = shouldThrow<IllegalArgumentException> {
                    authService.reIssueToken(refreshToken)
                }.message

                message shouldBe "토큰이 유효하지 않습니다."
            }
        }

        When("토큰이에 맞는 유저가 없을 때") {

            every { jwtService.isTokenValid(any()) } returns true
            every { userRepository.findByRefreshToken(refreshToken) } returns null
            every { jwtService.makeTokenResponse(user.email) } returns tokenResponse

            Then("재발행 실패") {
                val message = shouldThrow<ResourceNotFoundException> {
                    authService.reIssueToken(refreshToken)
                }.message

                message shouldBe "유저를 찾을 수 없습니다."
            }
        }
    }

})
