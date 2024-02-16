package kr.co.shophub.shophub.util

import kr.co.shophub.shophub.global.time.Time
import java.time.LocalDate

class FakeTime: Time {
    override fun now(): LocalDate {
        return LocalDate.of(2024, 12, 20)
    }
}