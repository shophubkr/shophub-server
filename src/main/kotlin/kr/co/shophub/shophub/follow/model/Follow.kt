package kr.co.shophub.shophub.follow.model

import jakarta.persistence.*
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.user.model.User

@Entity
class Follow (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    var id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    val shop: Shop,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

): BaseEntity()