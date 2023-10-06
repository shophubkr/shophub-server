package kr.co.shophub.shophub.global.exception

import kr.co.shophub.shophub.global.error.ResourceNotFoundException

fun failFindingUser(): Nothing {
    throw ResourceNotFoundException("유저를 찾을 수 없습니다.")
}

fun failExtractEmail(): Nothing {
    throw IllegalStateException("토큰이 올바르지 않습니다.")
}