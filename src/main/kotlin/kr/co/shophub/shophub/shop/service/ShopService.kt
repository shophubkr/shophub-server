package kr.co.shophub.shophub.shop.service

import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.shop.dto.*
import kr.co.shophub.shophub.shop.model.Shop
import kr.co.shophub.shophub.shop.model.ShopImage
import kr.co.shophub.shophub.shop.model.ShopTag
import kr.co.shophub.shophub.shop.repository.ShopImageRepository
import kr.co.shophub.shophub.shop.repository.ShopRepository
import kr.co.shophub.shophub.shop.repository.ShopTagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ShopService(
    private val shopRepository: ShopRepository,
    private val shopImageRepository: ShopImageRepository,
    private val shopTagRepository: ShopTagRepository
) {

    @Transactional
    fun createShop(sellerId: Long, createShopRequest: CreateShopRequest): ShopIdResponse {
        validateShopRequest(createShopRequest.images.size, createShopRequest.tags.size)

        val shop = Shop(createShopRequest, sellerId)
        val savedShop = shopRepository.save(shop)
        replaceShopImages(savedShop, createShopRequest.images)
        replaceShopTags(savedShop, createShopRequest.tags)

        return ShopIdResponse(savedShop.id)
    }

    fun getShop(shopId: Long): ShopResponse {
        val shop = findShop(shopId)
        return ShopResponse(shop)
    }

    fun getShopList(pageable: Pageable): Page<Shop> {
        return shopRepository.findAllByDeletedIsFalse(pageable)
    }

    @Transactional
    fun changeShop(shopId: Long, sellerId: Long, changeShopRequest: ChangeShopRequest): ShopIdResponse {
        validateShopRequest(changeShopRequest.images.size, changeShopRequest.tags.size)

        val shop = findShop(shopId)
        check(shop.sellerId == sellerId) { "변경 권한이 없습니다." }

        shop.changeShop(changeShopRequest)
        replaceShopImages(shop, changeShopRequest.images)
        replaceShopTags(shop, changeShopRequest.tags)
        return ShopIdResponse(shop.id)
    }

    @Transactional
    fun deleteShop(shopId: Long, sellerId: Long) {
        val shop = findShop(shopId)
        check(shop.sellerId == sellerId) { "삭제 권한이 없습니다." }
        shop.softDelete()
    }

    fun checkShopName(name: String): Boolean {
        return shopRepository.existsByNameAndDeletedIsFalse(name)
    }

    private fun replaceShopImages(shop: Shop, imageList: List<String>) {
        shop.deleteShopImages()
        val shopImages = imageList.map { ShopImage(imgUrl = it, shop = shop) }
        shopImageRepository.saveAll(shopImages)
        shop.addShopImages(shopImages)
    }

    private fun replaceShopTags(shop: Shop, tagList: List<String>) {
        shop.deleteShopTags()
        val shopTags = tagList.map { ShopTag(name = it, shop = shop) }
        shopTagRepository.saveAll(shopTags)
        shop.addShopTags(shopTags)
    }

    private fun validateShopRequest(imageSize: Int, tagSize: Int) {
        require(imageSize >= MIN_IMAGE_COUNT) { "이미지 최소 갯수는 $MIN_IMAGE_COUNT 개 입니다." }
        require(tagSize <= MAX_TAG_COUNT) { "태그 최대 갯수는 $MAX_TAG_COUNT 개 입니다." }
    }

    fun findShop(shopId: Long): Shop {
        return shopRepository.findByIdAndDeletedIsFalse(shopId)
            ?: throw ResourceNotFoundException("존재하지 않는 상점입니다.")
    }

    companion object {
        private const val MIN_IMAGE_COUNT = 3
        private const val MAX_TAG_COUNT = 5
    }
}
