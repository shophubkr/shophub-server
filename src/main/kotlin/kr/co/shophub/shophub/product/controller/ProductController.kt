package kr.co.shophub.shophub.product.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.product.dto.*
import kr.co.shophub.shophub.product.service.ProductService
import kr.co.shophub.shophub.shop.service.ShopService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    val productService: ProductService,
    val shopService: ShopService,
    val loginService: LoginService,
) {

    @PostMapping("/{shopId}")
    fun createProduct(
        @PathVariable shopId: Long,
        @RequestBody createProductRequest: CreateProductRequest,
    ): CommonResponse<ProductIdResponse> {

        return productService.createProduct(
            shopId = shopId,
            createProductRequest = createProductRequest,
        ).let { CommonResponse(it) }
    }

    /**
     * - Res 상품 조회
     *     * total
     *     * 등록 상품 [ ]
     *     * 페이지네이션
     *
     * ✔️ 등록 상품(Object)
     *     * idx
     *     * 대표 이미지
     *     * 1차 카테고리
     *     * 상품 소개
     *     * 가격
     */
    @GetMapping("/{productId}")
    fun getOneProduct(
        @PathVariable productId: Long,
    ): CommonResponse<ProductResponse> {

        return productService.getProduct(
            productId = productId,
        ).let {
            CommonResponse(it)
        }
    }

    @GetMapping
    fun getProductList(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): CommonResponse<ProductListResponse> {

        val productList = productService.getProductList(pageable)
        return CommonResponse(
            result = ProductListResponse(productList = productList.content.map { ProductResponse(it) }),
            page = PageInfo.of(productList),
        )
    }

    /**
     *
     * ✔️ 최근 등록 상품 (Object)
     *     * 상품 idx
     *     * 대표 이미지
     *     * 상품명
     *     * 상품 설명
     *     * 등록일자(YYYY-MM-DD)
     *     * 가격
     */

    /**
     * - Req 상품 수정
     *     * 이미지 [ ]
     *     * 상품 소개
     *     * 가격
     *     * 상품 태그 [ ]
     *     * 판매여부(판매중 | 판매안함)
     */
    @PutMapping("/{productId}")
    fun changeProduct(
        @PathVariable productId: Long,
        updateProductRequest: UpdateProductRequest,
    ): CommonResponse<ProductIdResponse> {

        return productService.changeProduct(
            productId = productId,
            updateProductRequest = updateProductRequest
        ).let { CommonResponse(it) }
    }

    /**
     * - Req 상품 삭제
     */
    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: String
    ) : CommonResponse<EmptyDto> {

        productService.deleteProduct(
            productId = productId
        )
        return CommonResponse.EMPTY
    }
}