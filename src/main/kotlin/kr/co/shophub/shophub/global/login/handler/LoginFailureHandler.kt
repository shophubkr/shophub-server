package kr.co.shophub.shophub.global.login.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import java.time.LocalDateTime

class LoginFailureHandler(
    private val objectMapper: ObjectMapper,
) : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json;charset=UTF-8"
        val data = mutableMapOf<String, Any>()
        data["timeStamp"] = LocalDateTime.now().toString().substring(0,19)
        data["message"] = "로그인에 실패 했습니다! 이메일이나 비밀번호를 확인하세요."
        response.writer.println(objectMapper.writeValueAsString(data))
    }

}

