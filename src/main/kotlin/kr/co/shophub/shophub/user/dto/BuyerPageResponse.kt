package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.shop.dto.ShopListResponse

data class BuyerPageResponse(
    val userInfo: UserInfo,
    val followShop: ShopListResponse,
    val coupon: MutableList<String>
)