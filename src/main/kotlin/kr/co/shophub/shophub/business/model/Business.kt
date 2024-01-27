package kr.co.shophub.shophub.business.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
class Business (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    var id: Long = 0L,

    @field:NotNull
    val businessNumber: String,

    val sellerId: Long,
    val shopId: Long,

)