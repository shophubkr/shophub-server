package kr.co.shophub.shophub.business.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.user.model.User

@Entity
class Business (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    var id: Long = 0L,

    @field:NotNull
    val businessNumber: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    var shop: Shop,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val seller: User

)