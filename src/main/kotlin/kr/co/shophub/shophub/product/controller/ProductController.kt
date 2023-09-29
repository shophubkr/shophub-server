package kr.co.shophub.shophub.product.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.product.dto.*
import kr.co.shophub.shophub.product.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    val productService: ProductService,
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

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody updateProductRequest: UpdateProductRequest,
    ): CommonResponse<ProductIdResponse> {

        return productService.updateProduct(
            productId = productId,
            updateProductRequest = updateProductRequest
        ).let { CommonResponse(it) }
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: Long
    ) : CommonResponse<EmptyDto> {

        productService.softDelete(
            productId = productId
        )
        return CommonResponse.EMPTY
    }
}