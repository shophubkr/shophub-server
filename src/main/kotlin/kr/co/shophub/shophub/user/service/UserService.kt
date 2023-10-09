package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.coupon.repository.CouponRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.exception.failFindingUser
import kr.co.shophub.shophub.shop.dto.ShopListResponse
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import kr.co.shophub.shophub.user.repository.UserCouponRepository
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
//    private val followRepository: FollowRepository,
    private val shopRepository: ShopRepository,
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
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

    fun getMyCoupons(userId: Long, status: UserCouponCond): UserCouponListResponse{
        val userCoupons =
            userCouponRepository.findUserCoupons(userId, status)
                .map { userCoupon -> UserCouponResponse(userCoupon) }
        return UserCouponListResponse(userCoupons)
    }

    @Transactional
    fun updateInfo(userId: Long, updateRequest: InfoUpdateRequest) {
        val user = getUser(userId)
        if (updateRequest.nickname != null) {
            user.updateNickname(updateRequest.nickname)
        }
        if (updateRequest.newPassword != null) {
            user.updatePassword(passwordEncoder, updateRequest.newPassword)
        }
    }

    fun checkPassword(request: PasswordRequest, userId: Long) {
        val user = getUser(userId)
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("비밀 번호가 일치하지 않습니다.")
        }
    }

    @Transactional
    fun updatePassword(updateRequest: PasswordUpdateRequest) {
        val user = userRepository.findByEmail(updateRequest.email) ?: failFindingUser()
        user.updatePassword(passwordEncoder, updateRequest.newPassword)
    }

    @Transactional
    fun deleteUser(userId: Long) {
        val user = getUser(userId)
        user.softDelete()
    }

    @Transactional
    fun receiveCoupon(couponId: Long, userId: Long): UserCouponIdResponse {
        val coupon = findCoupon(couponId)
        val user = getUser(userId)

        val saveUserCoupon = userCouponRepository.save(UserCoupon(user = user, coupon = coupon))
        user.userCoupon.add(saveUserCoupon)

        return UserCouponIdResponse(saveUserCoupon.id)
    }

    private fun findCoupon(couponId: Long) = couponRepository.findByCouponIdAndDeletedIsFalse(couponId)

    private fun getUser(userId: Long) =
        userRepository.findById(userId).getOrNull() ?: failFindingUser()

    @Transactional
    fun useCoupon(userCouponId: Long) {
        val userCoupon = (userCouponRepository.findById(userCouponId).getOrNull()
            ?: throw ResourceNotFoundException("유저 쿠폰 정보를 찾을 수 없습니다."))
        userCoupon.useCoupon()
    }
}