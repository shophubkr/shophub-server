package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.shop.dto.ShopListResponse
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.dto.InfoUpdateRequest
import kr.co.shophub.shophub.user.dto.MyPageResponse
import kr.co.shophub.shophub.user.dto.CheckResponse
import kr.co.shophub.shophub.user.dto.PasswordUpdateRequest
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
//    private val followRepository: FollowRepository,
    private val shopRepository: ShopRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun getMyPage(userId: Long): MyPageResponse {
        val user = getUser(userId)
        //팔로우 구현시 변경 예정
        val shops = shopRepository.findAll()
        val coupons = mutableListOf<String>()
        return MyPageResponse(
            email = user.email,
            followShop = ShopListResponse(shops.map { ShopSimpleResponse(it) }),
            coupon = coupons
        )
    }

    @Transactional
    fun updateInfo(userId: Long, updateRequest: InfoUpdateRequest) {
        val user = getUser(userId)
        user.updateInfo(updateRequest)
        user.encodePassword(passwordEncoder)
    }

    fun checkPassword(oldPassword: String, userId: Long): CheckResponse {
        val user = getUser(userId)
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            return CheckResponse("비밀번호가 일치하지 않습니다!")
        }
        return CheckResponse("비밀번호가 확인 되었습니다!")
    }

    @Transactional
    fun updatePassword(updateRequest: PasswordUpdateRequest) {
        val user = userRepository.findByEmail(updateRequest.email) ?: throw IllegalArgumentException("유저를 찾을 수 없습니다.")
        user.updatePassword(passwordEncoder, updateRequest.newPassword)
    }

    private fun getUser(userId: Long) =
        userRepository.findById(userId).getOrNull() ?: throw IllegalArgumentException("유저를 찾을 수 없습니다.")
}