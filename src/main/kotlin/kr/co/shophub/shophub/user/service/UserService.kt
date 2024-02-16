package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.coupon.dto.CouponResponse
import kr.co.shophub.shophub.coupon.dto.MyCouponResponse
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.coupon.repository.CouponRepository
import kr.co.shophub.shophub.follow.repository.FollowRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.time.Time
import kr.co.shophub.shophub.shop.dto.ShopListResponse
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.user.dto.*
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserCoupon
import kr.co.shophub.shophub.user.model.UserCouponCond
import kr.co.shophub.shophub.user.model.UserRole
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
    private val onServiceTime: Time,
) {

    fun getFollowShop(userId: Long): ShopListResponse {
        val user = getUser(userId)
        val followShopIds = user.userCoupon.map { it.coupon.shop.id }
        val shops = followRepository.findByUser(user)
            .map { ShopSimpleResponse(it.shop, followShopIds) }
        return ShopListResponse(shops)
    }

    fun getUserInfo(userId: Long, userRole: UserRole): UserInfo {
        val user = getUser(userId)
        checkRole(user.userRole, userRole)
        return UserInfo(user.email, user.profile)
    }

    private fun checkRole(userRole: UserRole, correctRole: UserRole) {
        if (userRole == UserRole.USER_SELLER) {
            throw IllegalArgumentException("가게 등록이 필요한 셀러입니다.")
        }
        if (userRole != correctRole) {
            throw IllegalArgumentException("올바른 유저 유형이 아닙니다.")
        }
    }

    fun getMyCoupons(
        userId: Long,
        status: UserCouponCond,
        pageable: Pageable,
        ): UserCouponListResponse{
        val userCoupons =
            userCouponRepository.findUserCoupons(userId, status, pageable, onServiceTime.now())
                .map { userCoupon -> UserCouponResponse(userCoupon) }

        return UserCouponListResponse(userCoupons, userCoupons.content.size)
    }

    fun getCouponCount(
        userId: Long
    ): MyCouponResponse {
        val findAllByUser = userCouponRepository.findAllByUser(getUser(userId))
            .map { CouponResponse(it.coupon, onServiceTime.now()) }

        val totalSize = findAllByUser.size
        val finishedSize = findAllByUser.filter { it.isFinished }.size

        return MyCouponResponse(
            findAllByUser,
            totalSize,
            totalSize - finishedSize,
            finishedSize
        )
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
        if (updateRequest.profile != null) {
            user.updateProfile(updateRequest.profile)
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
        return couponRepository.findByCouponId(couponId)
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
        check(!userRepository.existsByNickname(nicknameRequest.nickname)) { "해당 닉네임이 이미 존재합니다." }
    }
}