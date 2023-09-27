package kr.co.shophub.shophub.product.model.product

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.product.dto.CreateProductRequest
import kr.co.shophub.shophub.product.dto.UpdateProductRequest
import kr.co.shophub.shophub.product.model.category.ProductCategory
import kr.co.shophub.shophub.product.model.image.ProductImage
import kr.co.shophub.shophub.product.model.tag.ProductTag
import kr.co.shophub.shophub.shop.model.Shop

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    var shop: Shop,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<ProductImage> = mutableListOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tags: MutableList<ProductTag> = mutableListOf(),

    @OneToOne(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var category: ProductCategory?,

    private var deleted: Boolean = false

) : BaseEntity() {
    constructor(createProductRequest: CreateProductRequest, shop: Shop,) : this(
        name = createProductRequest.name,
        introduce = createProductRequest.introduce,
        price = createProductRequest.price,
        status = createProductRequest.status,
        shop = shop,
        category = null,
    )

    fun updateInfo(updateProductRequest: UpdateProductRequest) {
        name = updateProductRequest.name
        price = Integer.parseInt(updateProductRequest.price)
        introduce = updateProductRequest.introduce
        status = updateProductRequest.status
    }

    fun softDelete() {
        deleted = true
    }
}