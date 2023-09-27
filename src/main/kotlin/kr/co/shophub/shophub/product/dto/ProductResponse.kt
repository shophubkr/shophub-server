package kr.co.shophub.shophub.product.dto

import kr.co.shophub.shophub.product.model.product.Product

data class ProductResponse(
    val id: Long,
    val imageUrl: String,
    val topCategory: String,
    val introduce: String,
    val price: Int,
) {
    constructor(product: Product) : this(
        id = product.id,
        imageUrl = product.images[0].imgUrl,
        topCategory = product.category!!.name,
        introduce = product.introduce,
        price = product.price,
    )
}