package kr.co.shophub.shophub.follow.dto

import kr.co.shophub.shophub.shop.dto.ShopListResponse

data class FollowPageResponse (
    val shopListResponse: ShopListResponse,
    val followCount: Int,
)