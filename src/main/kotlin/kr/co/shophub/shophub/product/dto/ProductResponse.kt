package kr.co.shophub.shophub.product.dto

import kr.co.shophub.shophub.product.model.product.Product

data class ProductResponse(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val topCategory: String,
    val introduce: String,
    val price: Int,
) {
    constructor(product: Product) : this(
        id = product.id,
        name = product.name,
        imageUrl = product.getFirstImageUrl(),
        topCategory = product.category!!.name,
        introduce = product.introduce,
        price = product.price,
    )
}