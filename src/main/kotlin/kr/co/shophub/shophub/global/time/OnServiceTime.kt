package kr.co.shophub.shophub.global.time

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class OnServiceTime: Time {
    override fun now(): LocalDate {
        return LocalDate.now()
    }
}