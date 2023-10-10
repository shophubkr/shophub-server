package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.user.model.ProviderType
import kr.co.shophub.shophub.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?
    fun findByIdAndDeletedIsFalse(id: Long): User?
}