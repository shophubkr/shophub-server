package kr.co.shophub.shophub.shop.dto

import kr.co.shophub.shophub.shop.model.Shop

data class ShopSimpleResponse(
    val id: Long,
    val image: String,
    val name: String,
    val address: String,
    val introduce: String,
    val minPrice: Int,
    val checkCoupon: Boolean,
) {
    constructor(shop: Shop) : this(
        id = shop.id,
        image = shop.images[0].imgUrl,
        name = shop.name,
        address = shop.address,
        introduce = shop.introduce,
        minPrice = shop.products.minByOrNull { it.price }?.price ?: 0,
        checkCoupon = true
    )
}