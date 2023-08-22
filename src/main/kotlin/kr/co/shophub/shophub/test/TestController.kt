package kr.co.shophub.shophub.test

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

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
        throw Exception("test")
    }

    @GetMapping("/error2")
    fun getError2(): String {
        throw IllegalArgumentException("test")
    }

    @GetMapping("/error3")
    fun getError3(): String {
        val list = listOf("apple", "banana", "cherry")
        println(list[5])  // 존재하지 않는 인덱스에 접근
        return "하이"
    }
}
