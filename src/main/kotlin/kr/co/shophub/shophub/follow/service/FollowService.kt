package kr.co.shophub.shophub.follow.service

import kr.co.shophub.shophub.follow.model.Follow
import kr.co.shophub.shophub.follow.repository.FollowRepository
import kr.co.shophub.shophub.global.exception.failFindingShop
import kr.co.shophub.shophub.global.exception.failFindingUser
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

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

    fun getAllFollowShop(userId: Long): List<ShopSimpleResponse> {
        val user = getUser(userId)
        return followRepository.findByUser(user)
            .map { ShopSimpleResponse(it.shop) }
    }

    private fun getUser(userId: Long): User {
        return userRepository.findById(userId).getOrNull() ?: failFindingUser()
    }

    private fun getShop(shopId: Long): Shop {
        return shopRepository.findById(shopId).getOrNull() ?: failFindingShop()
    }

}