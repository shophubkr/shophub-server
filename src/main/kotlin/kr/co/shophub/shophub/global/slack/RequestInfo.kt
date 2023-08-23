package kr.co.shophub.shophub.global.slack

data class RequestInfo (
    val requestUrl: String,
    val method: String,
    val remoteAddr: String,
)