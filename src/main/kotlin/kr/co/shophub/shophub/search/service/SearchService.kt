package kr.co.shophub.shophub.search.service

import kr.co.shophub.shophub.product.dto.ProductResponse
import kr.co.shophub.shophub.product.repository.ProductTagRepository
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.repository.ShopTagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchService(
    private val shopTagRepository: ShopTagRepository,
    private val productTagRepository: ProductTagRepository
) {

    fun searchAllShopTag(
        search: String,
        pageable: Pageable
    ): Page<ShopSimpleResponse> {
       return shopTagRepository.searchAllShopTag(search, pageable)
           .map { ShopSimpleResponse(it) }
    }

    fun searchAllProductTag(
        search: String,
        pageable: Pageable
    ): Page<ProductResponse> {
        return  productTagRepository.searchAllProductTag(search, pageable)
            .map { ProductResponse(it) }
    }


}