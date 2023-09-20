package kr.co.shophub.shophub.shop.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.global.login.SecurityUtils.getLoginUserId
import kr.co.shophub.shophub.shop.dto.*
import kr.co.shophub.shophub.shop.service.ShopService
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/shops")
class ShopController(
    private val shopService: ShopService,
    private val userRepository: UserRepository
) {

    @PostMapping("")
    fun createShop(
        @RequestBody createShopRequest: CreateShopRequest
    ): CommonResponse<ShopIdResponse> {

        return shopService.createShop(
            sellerId = getLoginUserId(userRepository),
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
            result = ShopListResponse(shopList = shopList.content.map { ShopSimpleResponse(it) }),
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
            sellerId = getLoginUserId(userRepository),
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
            sellerId = getLoginUserId(userRepository)
        )
        return CommonResponse.EMPTY

    }
}