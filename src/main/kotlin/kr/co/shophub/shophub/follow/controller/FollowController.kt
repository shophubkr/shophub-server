package kr.co.shophub.shophub.follow.controller

import kr.co.shophub.shophub.follow.dto.FollowPageResponse
import kr.co.shophub.shophub.follow.service.FollowService
import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.shop.dto.ShopListResponse
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/follow")
class FollowController(
    private val loginService: LoginService,
    private val followService: FollowService,
) {

    @PostMapping("/{shopId}")
    fun followShop(@PathVariable shopId: Long): CommonResponse<EmptyDto> {

        val userId = loginService.getLoginUserId()
        followService.followShop(userId, shopId)
        return CommonResponse.EMPTY

    }

    @GetMapping("/shops")
    fun getAllFollowShop(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): CommonResponse<FollowPageResponse> {

        val userId = loginService.getLoginUserId()
        val followShopPage = followService.getAllFollowShop(userId, pageable)
        return CommonResponse(
            result = FollowPageResponse(
                shopListResponse = ShopListResponse(followShopPage.content.map { ShopSimpleResponse(it) }),
                followCount = followShopPage.content.size),
            page = PageInfo.of(followShopPage)
        )

    }
}