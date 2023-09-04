package kr.co.shophub.shophub.global.dto

import org.springframework.data.domain.Page

data class PageInfo(
    val size: Int,
    val number: Int,
    val totalElements: Long,
    val totalPages: Int
) {
    companion object {
        fun of(page: Page<*>): PageInfo =
            PageInfo(
                size = page.size,
                number = page.number,
                totalElements = page.totalElements,
                totalPages = page.totalPages
            )
    }
}
