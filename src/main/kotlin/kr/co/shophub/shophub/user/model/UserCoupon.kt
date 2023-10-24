package kr.co.shophub.shophub.user.model

import jakarta.persistence.*
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.global.model.BaseEntity

@Entity
class UserCoupon(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    var id: Long = 0L,

    private var isUsed: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    var coupon: Coupon

) : BaseEntity() {

    fun useCoupon() {
        isUsed = true
    }

}