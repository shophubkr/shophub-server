package kr.co.shophub.shophub.product.controller

import kr.co.shophub.shophub.global.dto.CommonResponse
import kr.co.shophub.shophub.global.dto.EmptyDto
import kr.co.shophub.shophub.global.dto.PageInfo
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.product.dto.*
import kr.co.shophub.shophub.product.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ProductController(
    val productService: ProductService,
    val loginService: LoginService,
) {

    @PostMapping("/{shopId}/products")
    fun createProduct(
        @PathVariable shopId: Long,
        @RequestBody createProductRequest: CreateProductRequest,
    ): CommonResponse<ProductIdResponse> {

        return productService.createProduct(
            loginUserId = loginService.getLoginUserId(),
            shopId = shopId,
            createProductRequest = createProductRequest,
        ).let { CommonResponse(it) }
    }

    @GetMapping("/products/{productId}")
    fun getOneProduct(
        @PathVariable productId: Long,
    ): CommonResponse<ProductResponse> {

        return productService.getProduct(
            productId = productId,
        ).let {
            CommonResponse(it)
        }
    }

    @GetMapping("/products/{productId}/images")
    fun getProductImages(
        @PathVariable productId: Long,
    ): CommonResponse<ProductImagesResponse> {

        return productService.getProductImages(
            productId = productId,
        ).let {
            CommonResponse(it)
        }
    }

    @GetMapping("/{shopId}/products")
    fun getProductList(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
        @PathVariable shopId: Long,
    ): CommonResponse<ProductListResponse> {

        val productList = productService.getProductList(shopId, pageable)
        return CommonResponse(
            result = ProductListResponse(productList = productList.content.map { ProductResponse(it) }),
            page = PageInfo.of(productList),
        )
    }

    @PutMapping("/products/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody updateProductRequest: UpdateProductRequest,
    ): CommonResponse<ProductIdResponse> {

        return productService.updateProduct(
            productId = productId,
            loginUserId = loginService.getLoginUserId(),
            updateProductRequest = updateProductRequest
        ).let { CommonResponse(it) }
    }

    @DeleteMapping("/products/{productId}")
    fun deleteProduct(
        @PathVariable productId: Long
    ) : CommonResponse<EmptyDto> {

        productService.softDelete(
            productId = productId,
            loginUserId = loginService.getLoginUserId()
        )
        return CommonResponse.EMPTY
    }
}