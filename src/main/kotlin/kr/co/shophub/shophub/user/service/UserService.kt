package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.coupon.repository.CouponRepository
import kr.co.shophub.shophub.follow.repository.FollowRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.dto.ShopListResponse
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import kr.co.shophub.shophub.user.repository.UserCouponRepository
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val userCouponRepository: UserCouponRepository,
    private val couponRepository: CouponRepository,
    private val followRepository: FollowRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun getMyPage(userId: Long): MyPageResponse {
        val user = getUser(userId)
        val shops = followRepository.findByUser(user)
            .map { ShopSimpleResponse(it.shop) }
        val coupons = mutableListOf<String>()
        return MyPageResponse(
            email = user.email,
            followShop = ShopListResponse(shops),
            coupon = coupons
        )
    }

    fun getMyCoupons(userId: Long, status: UserCouponCond, pageable: Pageable): UserCouponListResponse{
        val userCoupons =
            userCouponRepository.findUserCoupons(userId, status, pageable)
                .map { userCoupon -> UserCouponResponse(userCoupon) }

        return UserCouponListResponse(userCoupons, userCoupons.content.size)
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
        require(passwordEncoder.matches(request.password, user.password)) { "비밀 번호가 일치하지 않습니다." }
    }

    @Transactional
    fun updatePassword(updateRequest: PasswordUpdateRequest) {
        val user = userRepository.findByEmail(updateRequest.email)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
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
        user.addUserCoupon(saveUserCoupon)

        return UserCouponIdResponse(saveUserCoupon.id)
    }

    private fun findCoupon(couponId: Long): Coupon {
        return couponRepository.findByCouponIdAndDeletedIsFalse(couponId)
            ?: throw ResourceNotFoundException("쿠폰 정보를 찾을 수 없습니다.")
    }

    @Transactional
    fun useCoupon(userCouponId: Long) {
        val userCoupon = (userCouponRepository.findById(userCouponId).getOrNull()
            ?: throw ResourceNotFoundException("유저 쿠폰 정보를 찾을 수 없습니다."))
        userCoupon.useCoupon()
    }

    fun findSimpleInfo(userId: Long): SimpleInfoResponse {
        val user = getUser(userId)
        return SimpleInfoResponse(user.nickname, user.profile)
    }

    private fun getUser(userId: Long): User {
        return userRepository.findByIdAndDeletedIsFalse(userId)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
    }

    fun checkNickname(nicknameRequest: NicknameRequest) {
        require(!userRepository.existsByNickname(nicknameRequest.nickname)) { "해당 닉네임이 이미 존재합니다." }
    }
}