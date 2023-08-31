package kr.co.shophub.shophub.global.error

import kr.co.shophub.shophub.global.dto.CustomProblemDetail
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.lang.Nullable

class ForbiddenException(
    message: String? = "Forbidden.",
    @Nullable cause: Throwable? = null
) : RuntimeException(message, cause){

    fun getStatusCode(): HttpStatusCode {
        return HttpStatus.FORBIDDEN
    }

    fun getBody(): CustomProblemDetail {
        return CustomProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message!!)
    }

}
