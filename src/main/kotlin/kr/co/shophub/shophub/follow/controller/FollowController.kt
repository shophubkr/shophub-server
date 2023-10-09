package kr.co.shophub.shophub.follow.controller

import kr.co.shophub.shophub.follow.service.FollowService
import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.shop.dto.ShopListResponse
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

    @GetMapping("/shop")
    fun getAllFollowShop(): CommonResponse<ShopListResponse> {
        val userId = loginService.getLoginUserId()
        return CommonResponse(ShopListResponse(
            shopList = followService.getAllFollowShop(userId)
        ))
    }
}