package kr.co.shophub.shophub.shop.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.shop.dto.*
import kr.co.shophub.shophub.shop.service.ShopService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/shops")
class ShopController(
    private val shopService: ShopService,
    private val loginService: LoginService
) {

    @PostMapping("")
    fun createShop(
        @RequestBody createShopRequest: CreateShopRequest
    ): CommonResponse<ShopIdResponse> {

        return shopService.createShop(
            sellerId = loginService.getLoginUserId(),
            createShopRequest = createShopRequest
        )
            .let { CommonResponse(it) }

    }

    @GetMapping("/{shopId}")
    fun getShop(
        @PathVariable shopId: Long
    ): CommonResponse<ShopResponse> {

        return CommonResponse(shopService.getShop(shopId))

    }

    @GetMapping("")
    fun getShopList(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): CommonResponse<ShopListResponse> {

        val shopList = shopService.getShopList(pageable)
        return CommonResponse(
            result = ShopListResponse(shopList = shopList.content.map { ShopSimpleResponse(it, it.hasTerminateCoupon()) }),
            page = PageInfo.of(page = shopList)
        )
    }

    @PutMapping("/{shopId}")
    fun changeShop(
        @PathVariable shopId: Long,
        @RequestBody changeShopRequest: ChangeShopRequest
    ): CommonResponse<ShopIdResponse> {

        return shopService.changeShop(
            shopId = shopId,
            sellerId = loginService.getLoginUserId(),
            changeShopRequest = changeShopRequest
        )
            .let { CommonResponse(it) }

    }

    @DeleteMapping("/{shopId}")
    fun deleteShop(
        @PathVariable shopId: Long,
        authentication: Authentication
    ): CommonResponse<EmptyDto> {

        shopService.deleteShop(
            shopId = shopId,
            sellerId = loginService.getLoginUserId(),
        )
        return CommonResponse.EMPTY

    }

    @GetMapping("/check/{shopName}")
    fun checkShopName(
        @PathVariable shopName: String
    ): CommonResponse<Boolean> {

        return CommonResponse(shopService.checkShopName(shopName))

    }
}