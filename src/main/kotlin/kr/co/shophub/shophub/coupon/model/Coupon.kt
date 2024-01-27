package kr.co.shophub.shophub.coupon.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.coupon.dto.CreateCouponRequest
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.shop.model.Shop
import java.time.LocalDate

@Entity
class Coupon(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    var id: Long = 0L,

    @field:NotNull
    val content: String,

    @field:NotNull
    val detail: String,

    @field:NotNull
    val startedAt: LocalDate,

    @field:NotNull
    var expiredAt: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    var shop: Shop,

    ): BaseEntity() {

    constructor(createCouponRequest: CreateCouponRequest, shop: Shop): this(
        content = createCouponRequest.content,
        detail = createCouponRequest.detail,
        startedAt = createCouponRequest.startedAt,
        expiredAt = createCouponRequest.expiredAt,
        shop = shop,
    )

    fun terminateCoupon(now: LocalDate) {
        expiredAt = now.minusDays(1)
    }
}