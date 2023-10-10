package kr.co.shophub.shophub.follow.service

import kr.co.shophub.shophub.follow.model.Follow
import kr.co.shophub.shophub.follow.repository.FollowRepository
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.data.domain.Page
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

        if (followRepository.existsByShopAndUser(shop, user)) {
            followRepository.deleteByShopAndUser(shop, user)
            shop.cancelFollow()
        } else {
            val follow = Follow(shop = shop, user = user)
            followRepository.save(follow)
            shop.addFollow()
        }
    }

    fun getAllFollowShop(userId: Long, pageable: Pageable): Page<ShopSimpleResponse> {
        val user = getUser(userId)
        return followRepository.findByUser(user, pageable)
            .map { ShopSimpleResponse(it.shop) }
    }

    private fun getUser(userId: Long): User {
        return userRepository.findByIdAndDeletedIsFalse(userId)
            ?: throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
    }

    private fun getShop(shopId: Long): Shop {
        return shopRepository.findByIdAndDeletedIsFalse(shopId)
            ?: throw ResourceNotFoundException("매장을 찾을 수 없습니다.")
    }

}