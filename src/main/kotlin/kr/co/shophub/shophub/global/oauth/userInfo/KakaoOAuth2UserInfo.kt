package kr.co.shophub.shophub.global.oauth.userInfo

import java.util.UUID

class KakaoOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {

    override fun getId(): String {
        return attributes["id"].toString()
    }

    override fun getNickname(): String {
        val account = attributes["kakao_account"] as Map<*, *>
        val profile = account["profile"] as Map<*, *>

        return profile["nickname"] as String

    }

    override fun getEmail(): String {
        val account = attributes["kakao_account"] as Map<*, *>
        val substring = UUID.randomUUID().toString().substring(0, 7)
        return if (account["email"] == null) {
            "no-kakao-email-$substring@test.com"
        } else account["email"] as String
    }

    override fun getImageUrl(): String {
        val account = attributes["kakao_account"] as Map<*, *>
        val profile = account["profile"] as Map<*, *>

        return profile["thumbnail_image_url"] as String
    }
}