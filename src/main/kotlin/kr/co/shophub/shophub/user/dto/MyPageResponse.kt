package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.shop.model.Shop

data class MyPageResponse(
    val email: String,
    val followShop: MutableList<Shop>,
    val coupon: MutableList<String>
)