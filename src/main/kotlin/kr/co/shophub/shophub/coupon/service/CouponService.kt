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

@Service
@Transactional
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

        val coupon = Coupon(createCouponRequest, shop)
        shop.coupons.add(coupon)
        val saveCoupon = couponRepository.save(coupon)

        return CouponIdResponse(saveCoupon.id)
    }

    private fun findShop(shopId: Long) = (shopRepository.findByIdAndDeletedIsFalse(shopId)
        ?: throw ResourceNotFoundException("매장 정보를 찾을 수 없습니다."))

    @Transactional(readOnly = true)
    fun getCouponList(
        shopId: Long,
        pageable: Pageable,
        isFinished: Boolean
    ): Page<Coupon> {
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
    }

    private fun isOwnerOfShop(shop: Shop, loginUserId: Long) {
        check(shop.sellerId == loginUserId) { "매장에 대한 권한이 없습니다." }
    }

}