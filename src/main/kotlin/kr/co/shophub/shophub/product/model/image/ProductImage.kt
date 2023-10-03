package kr.co.shophub.shophub.product.model.image

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.product.model.product.Product

@Entity
class ProductImage(
    @Id @Column(name = "product_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @field:NotNull
    var imgUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product
) : BaseEntity() {

}