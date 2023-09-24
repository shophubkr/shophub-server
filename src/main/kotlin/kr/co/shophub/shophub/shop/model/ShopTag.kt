package kr.co.shophub.shophub.shop.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.global.model.BaseEntity

@Entity
class ShopTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_tag_id")
    val id: Long = 0L,

    @field:NotNull
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    var shop: Shop
) : BaseEntity()