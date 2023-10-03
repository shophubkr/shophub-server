package kr.co.shophub.shophub.global.exception

import java.lang.IllegalArgumentException

fun failFindingUser(): Nothing {
    throw IllegalArgumentException("유저를 찾을 수 없습니다.")
}

fun failExtractEmail(): Nothing {
    throw IllegalStateException("토큰이 올바르지 않습니다.")
}