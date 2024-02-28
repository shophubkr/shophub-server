package kr.co.shophub.shophub.search.service

import kr.co.shophub.shophub.search.model.SortBy
import kr.co.shophub.shophub.shop.dto.ShopSimpleResponse
import kr.co.shophub.shophub.shop.repository.ShopTagRepository
import kr.co.shophub.shophub.user.model.QUserCoupon
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchService(
    private val shopTagRepository: ShopTagRepository,
) {

    fun searchAllShopWithShopTagAndProductTag(
        search: String,
        hasCoupon: Boolean?,
        sortBy: SortBy?,
        pageable: Pageable
    ): Page<ShopSimpleResponse> {
       return shopTagRepository.searchAllShopWithShopTagAndProductTag(
           search = search,
           hasCoupon = hasCoupon,
           sortBy = sortBy,
           pageable = pageable
       )
           .map { ShopSimpleResponse(it) }
    }
}