package kr.co.shophub.shophub.global.error

import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.stereotype.Component
import java.lang.Exception

//@Component
//class FeignErrorDecoder : ErrorDecoder {
//    override fun decode(methodKey: String?, response: Response?): Exception {
//        return when (response?.status()) {
//            400 -> IllegalArgumentException("Bad Request: $methodKey")
//            404 -> ResourceNotFoundException("Not Found: $methodKey")
//            // feign client 라는 오류로 표시
//
//            else -> FeignException.errorStatus(methodKey, response)
//        }
//    }
//}