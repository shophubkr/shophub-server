package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.shop.dto.ShopListResponse

data class MyPageResponse(
    val email: String,
    val followShop: ShopListResponse,
    val coupon: MutableList<String>
)