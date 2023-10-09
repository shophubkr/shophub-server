package kr.co.shophub.shophub.follow.repository

import kr.co.shophub.shophub.follow.model.Follow
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying

interface FollowRepository : JpaRepository<Follow, Long> {

    fun existByShopAndUser(shop: Shop, user: User): Boolean

    fun findByUser(user: User): List<Follow>

    @Modifying(flushAutomatically = true)
    fun deleteByShopAndUser(shop: Shop, user: User)
}