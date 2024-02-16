package kr.co.shophub.shophub.global.time

import java.time.LocalDate

interface Time {
    fun now(): LocalDate
}