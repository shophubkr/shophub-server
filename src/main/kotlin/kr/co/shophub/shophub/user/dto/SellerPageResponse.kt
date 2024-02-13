package kr.co.shophub.shophub.user.dto

import kr.co.shophub.shophub.coupon.dto.MyCouponResponse
import kr.co.shophub.shophub.shop.dto.MyShopResponse

data class SellerPageResponse(
    val userInfo: UserInfo,
    val myShop: List<MyShopResponse>,
    val myCoupon: MyCouponResponse
)