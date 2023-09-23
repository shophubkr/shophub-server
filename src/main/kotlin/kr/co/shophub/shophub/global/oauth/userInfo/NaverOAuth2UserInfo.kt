package kr.co.shophub.shophub.global.oauth.userInfo

class NaverOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        val response = attributes["response"] as Map<*, *>

        return (response["id"] as String)
    }

    override fun getNickname(): String {
        val response = attributes["response"] as Map<*, *>

        return (response["name"] as String)
    }

    override fun getEmail(): String {
        val response = attributes["response"] as Map<*, *>
        return (response["email"] as String)
    }

    override fun getImageUrl(): String {
        val response = attributes["response"] as Map<*, *>
        return (response["profile_image"] as String)
    }
}