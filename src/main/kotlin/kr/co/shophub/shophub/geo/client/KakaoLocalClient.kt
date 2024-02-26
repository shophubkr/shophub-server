package kr.co.shophub.shophub.geo.client

import kr.co.shophub.shophub.geo.dto.KakaoAddressResult
import kr.co.shophub.shophub.global.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kakaoLocal", url = "https://dapi.kakao.com/v2/local", configuration = [FeignConfig::class])
interface KakaoLocalClient {

    /**
     * 주소 검색
     * https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord
     */
    @GetMapping("/search/address.json")
    fun searchAddress(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam("query") query: String,
        @RequestParam("analyze_type") analyzeType: String = "exact",
    ): KakaoAddressResult

}