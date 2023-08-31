package kr.co.shophub.shophub.global.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CommonResponse<T : Any>(
    val result: T,
    val page: PageInfo? = null
) {

    companion object {
        val EMPTY = CommonResponse(EmptyDto())
    }

}
