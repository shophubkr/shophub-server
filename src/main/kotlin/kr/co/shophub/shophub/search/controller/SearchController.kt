package kr.co.shophub.shophub.search.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.search.model.SortBy
import kr.co.shophub.shophub.search.service.SearchService
import kr.co.shophub.shophub.shop.dto.ShopListResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchService : SearchService
) {

    @GetMapping("/shops")
    fun searchShop(
        @RequestParam search: String,
        @RequestParam(required = false) hasCoupon: Boolean?,
        @RequestParam(required = false) sortBy: String?,
        pageable: Pageable
    ): CommonResponse<ShopListResponse> {
        val searchList = searchService.searchAllShopWithShopTagAndProductTag(
            search = search,
            hasCoupon = hasCoupon,
            sortBy = sortBy?.let { SortBy.valueOf(it) },
            pageable = pageable
        )

        return CommonResponse(
            result = ShopListResponse(shopList = searchList.content),
            page = PageInfo.of(page = searchList)
        )
    }
}
