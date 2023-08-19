package kr.co.shophub.shophub.test

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class SimpleController {

    companion object {
        private val logger = LoggerFactory.getLogger(SimpleController::class.java)
    }

    @GetMapping("/info")
    fun getInfo(): String {
        logger.info("This is an info log")
        return "INFO"
    }

    @GetMapping("/error")
    fun getError(): String {
        logger.error("This is an error log")
        return "ERROR"
    }
}
