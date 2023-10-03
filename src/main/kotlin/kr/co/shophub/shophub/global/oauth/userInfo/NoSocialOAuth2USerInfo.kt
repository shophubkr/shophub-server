package kr.co.shophub.shophub.global.oauth.userInfo

class NoSocialOAuth2USerInfo(attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return "only-social"
    }

    override fun getNickname(): String {
        return "only-social"
    }

    override fun getEmail(): String {
        return "only-social"
    }

    override fun getImageUrl(): String {
        return "only-social"
    }
}