package kr.co.shophub.shophub.follow.dto

import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import org.springframework.data.domain.Page

data class FollowPageResponse (
    private val shopListResponse: Page<ShopSimpleResponse>,
    private val followCount: Int,
)