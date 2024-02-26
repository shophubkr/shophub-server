package kr.co.shophub.shophub.shop.repository

import kr.co.shophub.shophub.search.model.SortBy
import kr.co.shophub.shophub.shop.model.Shop
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


fun interface ShopTagRepositoryCustom {

    fun searchAllShopWithShopTagAndProductTag(
        search: String,
        hasCoupon: Boolean?,
        sortBy: SortBy?,
        pageable: Pageable
    ) : Page<Shop>
}