package kr.co.shophub.shophub.shop.dto

import kr.co.shophub.shophub.shop.model.Shop
data class ShopResponse(
    val id: Long,
    val sellerId: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val hour: String,
    val tags: List<String>,
    val images: List<String>,
    val telNum: String,
    val introduce: String,
    val level: Int,
    val followCnt: Int
) {
    constructor(shop: Shop) : this(
        id = shop.id,
        sellerId = shop.sellerId,
        name = shop.name,
        latitude = shop.latitude,
        longitude = shop.longitude,
        address = shop.address,
        hour = shop.hour,
        tags = shop.tags.map { it.name },
        images = shop.images.map { it.imgUrl },
        telNum = shop.telNum,
        introduce = shop.introduce,
        level = shop.level,
        followCnt = shop.followCnt
    )
}