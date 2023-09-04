package kr.co.shophub.shophub.global.jwt.error

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.dto.CustomProblemDetail
import kr.co.shophub.shophub.global.error.ForbiddenException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.FORBIDDEN.value()

        val problemDetail = CustomProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            ForbiddenException("자신의 것만 가능합니다").message!!
        )

        val errorBody = mapOf(
            "error" to problemDetail
        )

        val body = objectMapper.writeValueAsString(errorBody)
        response.writer.write(body)
    }
}