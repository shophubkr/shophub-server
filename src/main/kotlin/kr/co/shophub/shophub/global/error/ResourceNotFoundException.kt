package kr.co.shophub.shophub.global.error

import kr.co.shophub.shophub.global.dto.CustomProblemDetail
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.lang.Nullable

class ResourceNotFoundException(
    message: String? = "Resource not found.",
    @Nullable cause: Throwable? = null
) : RuntimeException(message, cause) {

    fun getStatusCode(): HttpStatusCode {
        return HttpStatus.NOT_FOUND
    }

    fun getBody(): CustomProblemDetail {
        return CustomProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message!!)
    }

}
