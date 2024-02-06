package kr.co.shophub.shophub.global.oauth.userInfo

import kr.co.shophub.shophub.global.util.NicknameGenerator

class GoogleOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return attributes["sub"] as String
    }

    override fun getNickname(): String {
        return NicknameGenerator.makeNickname()
    }

    override fun getEmail(): String {
        return attributes["email"] as String
    }

    override fun getImageUrl(): String {
        return attributes["picture"] as String
    }
}
