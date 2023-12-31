package kr.co.shophub.shophub.product.dto

import kr.co.shophub.shophub.product.model.product.ProductStatus

data class CreateProductRequest(
    val name: String,
    val price: Int,
    val introduce: String,
    val tags: List<String>,
    val images: List<String>,
    val status: ProductStatus,
    val category: String,
){

}