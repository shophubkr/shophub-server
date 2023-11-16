package kr.co.shophub.shophub.global

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/")
    fun health(): String {
        return "Shophub REST API Server is running!"
    }
}