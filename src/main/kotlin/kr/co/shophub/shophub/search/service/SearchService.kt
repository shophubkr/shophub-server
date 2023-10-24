package kr.co.shophub.shophub.search.service

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
) {

    fun searchAllShopWithShopTanAndProductTag(
        search: String,
        pageable: Pageable
    ): Page<ShopSimpleResponse> {
       return shopTagRepository.searchAllShopWithShopTagAndProductTag(search, pageable)
           .map { ShopSimpleResponse(it) }
    }
}