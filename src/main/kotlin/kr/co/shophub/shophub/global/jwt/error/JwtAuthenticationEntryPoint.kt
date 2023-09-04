package kr.co.shophub.shophub.global.jwt.error

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.dto.CustomProblemDetail
import kr.co.shophub.shophub.global.error.UnauthenticatedException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.UNAUTHORIZED.value()

        val problemDetail = CustomProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            UnauthenticatedException("accessToken이 만료되었습니다. 다시 로그인해주세요.").message!!
        )

        val errorBody = mapOf(
            "error" to problemDetail
        )

        val body = objectMapper.writeValueAsString(errorBody)
        response.writer.write(body)
    }
}
