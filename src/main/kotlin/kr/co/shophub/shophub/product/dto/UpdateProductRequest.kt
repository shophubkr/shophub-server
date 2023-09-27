package kr.co.shophub.shophub.product.dto

import kr.co.shophub.shophub.product.model.product.ProductStatus

data class UpdateProductRequest(
    val name: String,
    val price: String,
    val introduce: String,
    val images: List<String>,
    val tags: List<String>,
    val status: ProductStatus,
){

}