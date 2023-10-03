package kr.co.shophub.shophub.product.model.category

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.product.model.product.Product

@Entity
class ProductCategory(

    @Id
    @Column(name = "product_category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @field:NotNull
    var name: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product")
    var product: Product,

//    @OneToMany(mappedBy = "productCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val subCategories: MutableList<ProductDetailCategory> = mutableListOf()
) : BaseEntity(){
}