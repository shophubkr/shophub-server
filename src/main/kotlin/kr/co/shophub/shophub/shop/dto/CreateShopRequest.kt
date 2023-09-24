package kr.co.shophub.shophub.shop.dto

data class CreateShopRequest(
    val name : String,
    val tags : List<String>,
    val images : List<String>,
    val address : String,
    val introduce : String,
    val hour : String,
    val hourDescription : String,
    val telNum : String
)