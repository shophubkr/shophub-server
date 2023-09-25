package kr.co.shophub.shophub.shop.repository

import kr.co.shophub.shophub.shop.model.ShopTag
import org.springframework.data.jpa.repository.JpaRepository

interface ShopTagRepository : JpaRepository<ShopTag, Long> {
}