package kr.co.shophub.shophub.shop.dto

import kr.co.shophub.shophub.shop.model.Shop

data class ShopSimpleResponse(
    val id: Long,
    val image: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val introduce: String,
    val minPrice: Int,
    val checkCoupon: Boolean,
) {
    constructor(shop: Shop, checkCoupon: Boolean) : this(
        id = shop.id,
        image = shop.images[0].imgUrl,
        name = shop.name,
        latitude = shop.latitude,
        longitude = shop.longitude,
        address = shop.address,
        introduce = shop.introduce,
        minPrice = shop.products.minByOrNull { it.price }?.price ?: 0,
        checkCoupon =  checkCoupon
    )

    constructor(shop: Shop, ids: List<Long>) : this(
        id = shop.id,
        image = shop.images[0].imgUrl,
        name = shop.name,
        latitude = shop.latitude,
        longitude = shop.longitude,
        address = shop.address,
        introduce = shop.introduce,
        minPrice = shop.products.minByOrNull { it.price }?.price ?: 0,
        checkCoupon = ids.contains(shop.id)
    )
}