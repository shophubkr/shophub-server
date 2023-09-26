package kr.co.shophub.shophub.shop.repository

import kr.co.shophub.shophub.shop.model.ShopImage
import org.springframework.data.jpa.repository.JpaRepository

interface ShopImageRepository : JpaRepository<ShopImage, Long> {
}