package kr.co.shophub.shophub.product.model.tag

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.product.model.product.Product

@Entity
class ProductTag (
    @Id @Column(name = "product_tag")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @field:NotNull
    var tag: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product
) : BaseEntity() {
}