package kr.co.shophub.shophub.shop.dto

import kr.co.shophub.shophub.product.dto.ProductResponse

data class MyShopResponse(
    val shopName: String,
    val recentlyAddedItem: List<ProductResponse>,
)