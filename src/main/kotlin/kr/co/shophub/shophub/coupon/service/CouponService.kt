package kr.co.shophub.shophub.coupon.service

import kr.co.shophub.shophub.coupon.dto.CouponIdResponse
import kr.co.shophub.shophub.coupon.dto.CreateCouponRequest
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.coupon.repository.CouponRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class CouponService(
    private val couponRepository: CouponRepository,
    private val shopRepository: ShopRepository,
) {

    @Transactional
    fun createCoupon(
        createCouponRequest: CreateCouponRequest,
        userId: Long,
        shopId: Long,
    ): CouponIdResponse {
        val shop = findShop(shopId)
        isOwnerOfShop(shop, userId)

        validateCreateCouponRequest(createCouponRequest.startedAt, createCouponRequest.expiredAt)

        val coupon = Coupon(createCouponRequest, shop)
        shop.coupons.add(coupon)
        val saveCoupon = couponRepository.save(coupon)

        return CouponIdResponse(saveCoupon.id)
    }

    private fun validateCreateCouponRequest(startedAt: LocalDate, expiredAt: LocalDate) {
        require(startedAt.isBefore(expiredAt)) {"기간 설정이 잘못 되었습니다."}
    }

    private fun findShop(shopId: Long) = (shopRepository.findByIdAndDeletedIsFalse(shopId)
        ?: throw ResourceNotFoundException("매장 정보를 찾을 수 없습니다."))

    private fun isNotShopExist(shopId: Long) {
        if (!shopRepository.existsByIdAndDeletedIsFalse(shopId)) {
            throw ResourceNotFoundException("매장 정보를 찾을 수 없습니다.")
        }
    }

    fun getCouponList(
        shopId: Long,
        isFinished: Boolean,
        pageable: Pageable
    ): Page<Coupon> {
        isNotShopExist(shopId)
        return couponRepository.findAllByShopIdAndIsTerminatedAndDeletedIsFalse(shopId, isFinished, pageable)
    }

    @Transactional
    fun terminateCoupon(
        couponId: Long,
        userId: Long,
    ) {
        val coupon = findCoupon(couponId)
        isOwnerOfShop(coupon.shop, userId)
        coupon.terminateCoupon()
    }

    private fun findCoupon(couponId: Long): Coupon {
        return couponRepository.findByCouponIdAndDeletedIsFalse(couponId)
            ?: throw ResourceNotFoundException("쿠폰 정보를 찾을 수 없습니다.")
    }

    private fun isOwnerOfShop(shop: Shop, loginUserId: Long) {
        check(shop.sellerId == loginUserId) { "매장에 대한 권한이 없습니다." }
    }

}