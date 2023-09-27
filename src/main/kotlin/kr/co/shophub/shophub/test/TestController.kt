package kr.co.shophub.shophub.test

import kr.co.shophub.shophub.global.error.ForbiddenException
import kr.co.shophub.shophub.global.error.ResourceNotFoundException
import kr.co.shophub.shophub.global.error.UnauthenticatedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
) {
    @GetMapping("/illegal-argument")
    fun triggerIllegalArgumentException(): String {
        throw IllegalArgumentException("Illegal argument provided!")
    }

    @GetMapping("/unauthenticated")
    fun triggerUnauthenticatedException(): String {
        throw UnauthenticatedException("User is not authenticated!")
    }


    @GetMapping("/forbidden")
    fun triggerForbiddenException(): String {
        throw ForbiddenException("Access is forbidden!")
    }

    @GetMapping("/resource-not-found")
    fun triggerResourceNotFoundException(): String {
        throw ResourceNotFoundException("Resource not found!")
    }

    @GetMapping("/illegal-state")
    fun triggerIllegalStateException(): String {
        throw IllegalStateException("Illegal state detected!")
    }

    @GetMapping("/custom")
    fun triggerCustomException(): String {
        throw CustomException("This is a custom exception!")
    }
    class CustomException(message: String) : RuntimeException(message)

    @GetMapping("/authentication")
    fun triggerMyAuthenticationException(): String {
        throw MyAuthenticationException("Authentication failed!")
    }

    class MyAuthenticationException(message: String) : AuthenticationException(message)
}