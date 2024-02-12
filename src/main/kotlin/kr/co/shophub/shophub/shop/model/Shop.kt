package kr.co.shophub.shophub.shop.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kr.co.shophub.shophub.coupon.model.Coupon
import kr.co.shophub.shophub.global.model.BaseEntity
import kr.co.shophub.shophub.product.model.product.Product
import kr.co.shophub.shophub.shop.dto.ChangeShopRequest
import kr.co.shophub.shophub.shop.dto.CreateShopRequest

@Entity
class Shop(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    var id: Long = 0L,

    @field:NotNull
    var name: String,

    @field:NotNull
    var address: String,

    @field:NotNull
    var telNum: String,

    @field:NotNull
    @Size(max = 200)
    var introduce: String,

    var hour: String,
    @Size(max = 20)
    var hourDescription: String,

    var level: Int = 1,
    var followCnt: Int = 0,

    @field:NotNull
    val sellerId: Long,

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    var products: MutableList<Product> = mutableListOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    var coupons: MutableList<Coupon> = mutableListOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    var images: MutableList<ShopImage> = mutableListOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tags: MutableList<ShopTag> = mutableListOf(),

    @Column(name = "is_deleted")
    private var deleted: Boolean = false

) : BaseEntity() {
    constructor(createShopRequest: CreateShopRequest, sellerId: Long) : this(
        name = createShopRequest.name,
        address = createShopRequest.address,
        telNum = createShopRequest.telNum,
        introduce = createShopRequest.introduce,
        hour = createShopRequest.hour,
        hourDescription = createShopRequest.hourDescription,
        followCnt = 0,
        sellerId = sellerId
    )

    fun changeShop(changeShopRequest: ChangeShopRequest) {
        name = changeShopRequest.name
        address = changeShopRequest.address
        telNum = changeShopRequest.telNum
        introduce = changeShopRequest.introduce
        hour = changeShopRequest.hour
        hourDescription = changeShopRequest.hourDescription
    }

    fun addShopImages(image: List<ShopImage>) {
        images.addAll(image)
    }

    fun addShopTags(tag: List<ShopTag>) {
        tags.addAll(tag)
    }

    fun deleteShopImages() {
        images.removeAll(images)
    }

    fun deleteShopTags() {
        tags.removeAll(tags)
    }

    fun softDelete() {
        deleted = true
    }

    fun addProduct(product: Product) {
        products.add(product)
    }
    
    fun addFollow() {
        this.followCnt++
        this.level = followCnt/10 + 1
    }

    fun cancelFollow() {
        this.followCnt--
        this.level = followCnt/10 + 1
    }

    fun addCoupon(coupon: Coupon) {
        this.coupons.add(coupon)
    }
}