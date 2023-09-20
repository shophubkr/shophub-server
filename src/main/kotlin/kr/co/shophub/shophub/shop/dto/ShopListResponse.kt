package kr.co.shophub.shophub.shop.dto

import org.springframework.data.domain.Page

data class ShopListResponse(
    val shopList: List<ShopSimpleResponse>
)