package kr.co.shophub.shophub.shop.dto

import kr.co.shophub.shophub.shop.model.Shop

data class ShopSimpleResponse(
    val id: Long,
    val name: String,
    val address: String,
    val introduce: String,
    val checkCoupon: Boolean,
){
    constructor(shop: Shop) : this(
        id = shop.id,
        name = shop.name,
        address = shop.address,
        introduce = shop.introduce,
        checkCoupon = true
    )
}
