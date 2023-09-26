package kr.co.shophub.shophub.global.oauth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.dto.CustomProblemDetail
import kr.co.shophub.shophub.global.error.UnauthenticatedException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

/**
 * 삭제해도 될 것 같은데 일단 뒀습니다.
 */
@Component
class OAuth2LoginFailureHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.UNAUTHORIZED.value()

        val problemDetail = CustomProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            UnauthenticatedException("인증에 실패했습니다.").message!!
        )

        val errorBody = mapOf(
            "error" to problemDetail
        )

        val body = objectMapper.writeValueAsString(errorBody)
        response.writer.write(body)

    }
}