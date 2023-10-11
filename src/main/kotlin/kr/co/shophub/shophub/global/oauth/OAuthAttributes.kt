package kr.co.shophub.shophub.global.oauth

import kr.co.shophub.shophub.global.oauth.userInfo.*
import kr.co.shophub.shophub.global.oauth.util.PasswordUtil
import kr.co.shophub.shophub.user.model.ProviderType
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserRole

class OAuthAttributes(
    val nameAttributeKey: String,
    val oAuth2UserInfo: OAuth2UserInfo,
) {

    companion object {
        fun of(socialType: ProviderType,
               userNameAttributeName: String,
               attributes: MutableMap<String, Any>,
        ): OAuthAttributes {
            return when (socialType) {
                ProviderType.NAVER -> ofNaver(userNameAttributeName, attributes)
                ProviderType.KAKAO -> ofKakao(userNameAttributeName, attributes)
                ProviderType.GOOGLE -> ofGoogle(userNameAttributeName, attributes)
                ProviderType.NO_SOCIAL -> ofNoSocial(userNameAttributeName, attributes)
            }
        }


        private fun ofNaver(userNameAttributeName: String, attributes: MutableMap<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                userNameAttributeName,
                NaverOAuth2UserInfo(attributes),
            )
        }

        private fun ofKakao(userNameAttributeName: String, attributes: MutableMap<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                userNameAttributeName,
                KakaoOAuth2UserInfo(attributes),
            )
        }

        private fun ofGoogle(userNameAttributeName: String, attributes: MutableMap<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                userNameAttributeName,
                GoogleOAuth2UserInfo(attributes),
            )
        }

        private fun ofNoSocial(userNameAttributeName: String, attributes: MutableMap<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                userNameAttributeName,
                NoSocialOAuth2USerInfo(attributes),
            )
        }
    }

    fun toEntity(providerType: ProviderType, oAuth2UserInfo: OAuth2UserInfo): User {
        return User(
            nickname = oAuth2UserInfo.getNickname(),
            providerId = oAuth2UserInfo.getId(),
            providerType = providerType,
            email = oAuth2UserInfo.getEmail(),
            profile = oAuth2UserInfo.getImageUrl(),
            userRole = UserRole.GUEST_BUYER,
            password = PasswordUtil.generateRandomPassword(),
            telNum = ""
        )
    }

}