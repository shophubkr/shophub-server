package kr.co.shophub.shophub.global.exception

import jakarta.servlet.http.HttpServletRequest
import kr.co.shophub.shophub.global.slack.SlackNotification
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @SlackNotification
    fun handleException(
        req: HttpServletRequest,
        e: Exception
    ): ResponseEntity<Map<String, Any>> {

        val body = mapOf(
            "error" to "Internal Server Error",
            "message" to (e.message ?: "Unknown error occurred."),
            "path" to req.requestURI
        )

        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}