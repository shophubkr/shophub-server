package kr.co.shophub.shophub.follow.service

import kr.co.shophub.shophub.follow.dto.SortCondition
import kr.co.shophub.shophub.follow.model.Follow
import kr.co.shophub.shophub.follow.repository.FollowRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FollowService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
) {

    @Transactional
    fun followShop(userId: Long, shopId: Long) {
        val user = getUser(userId)
        val shop = getShop(shopId)

        if (isAlreadyFollow(shop, user)) {
            followRepository.deleteByShopAndUser(shop, user)
            shop.cancelFollow()
        } else {
            val follow = Follow(shop = shop, user = user)
            followRepository.save(follow)
            shop.addFollow()
        }
    }

    private fun isAlreadyFollow(
        shop: Shop,
        user: User
    ) = followRepository.existsByShopAndUser(shop, user)

    fun getAllFollowShop(userId: Long, pageable: Pageable, condition: SortCondition): Page<ShopSimpleResponse> {
        val user = getUser(userId)
        val ids = user.userCoupon.map { it.coupon.shop.id }
        var followedShops = followRepository.findByUser(user)
            .map { ShopSimpleResponse(it.shop, ids) }
            .sortedByDescending { it.id }

        if (condition.hasCoupon) {
            followedShops = filterByHasCoupon(followedShops)
        }
        if (condition.isSortByMinPrice) {
            followedShops = sortByMinPrice(followedShops)
        }

        return createPage(followedShops, pageable)
    }

    private fun createPage(findByUser: List<ShopSimpleResponse>, pageable: Pageable): Page<ShopSimpleResponse> {
        return PageImpl(findByUser, pageable, findByUser.size.toLong())
    }

    private fun filterByHasCoupon(list: List<ShopSimpleResponse>): List<ShopSimpleResponse> =
        list.filter { it.checkCoupon }

    private fun sortByMinPrice(list: List<ShopSimpleResponse>): List<ShopSimpleResponse> =
        list.sortedBy { it.minPrice }

    private fun getUser(userId: Long): User {
        return userRepository.findByIdAndDeletedIsFalse(userId)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
    }

    private fun getShop(shopId: Long): Shop {
        return shopRepository.findByIdAndDeletedIsFalse(shopId)
            ?: throw ResourceNotFoundException("매장을 찾을 수 없습니다.")
    }

}