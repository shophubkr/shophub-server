package kr.co.shophub.shophub.product.dto

import kr.co.shophub.shophub.product.model.product.ProductStatus

data class UpdateProductRequest(
    val name: String,
    val price: Int,
    val introduce: String,
    val images: List<String>,
    val status: ProductStatus,
){

}