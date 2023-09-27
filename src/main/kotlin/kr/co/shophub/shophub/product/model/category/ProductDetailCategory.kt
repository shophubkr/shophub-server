package kr.co.shophub.shophub.product.model.category

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.global.model.BaseEntity

@Entity
class ProductDetailCategory (

    @Id
    @Column(name = "product_category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @field:NotNull
    var name: String,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_category")
//    var productCategory: ProductCategory
) : BaseEntity(){
}