package kr.co.shophub.shophub.product.model.product

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.product.model.category.ProductCategory
import kr.co.shophub.shophub.product.model.image.ProductImage
import kr.co.shophub.shophub.product.model.tag.ProductTag

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    var id: Long = 0L,

    @field:NotNull
    var name: String,

    @field:NotNull
    var introduce: String,

    @field:NotNull @field:PositiveOrZero
    var price: Int,

    @Enumerated(EnumType.STRING)
    var status: ProductStatus,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<ProductImage> = mutableListOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tags: MutableList<ProductTag> = mutableListOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var categories: MutableList<ProductCategory> = mutableListOf(),

    var isDeleted: Boolean

) : BaseEntity() {


}