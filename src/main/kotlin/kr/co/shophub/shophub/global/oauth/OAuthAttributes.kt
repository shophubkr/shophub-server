package kr.co.shophub.shophub.global.oauth

import kr.co.shophub.shophub.global.oauth.util.PasswordUtil
import kr.co.shophub.shophub.global.oauth.userInfo.GoogleOAuth2UserInfo
import kr.co.shophub.shophub.global.oauth.userInfo.KakaoOAuth2UserInfo
import kr.co.shophub.shophub.global.oauth.userInfo.NaverOAuth2UserInfo
import kr.co.shophub.shophub.global.oauth.userInfo.OAuth2UserInfo
import kr.co.shophub.shophub.user.model.ProviderType
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.model.UserRole


/**
 * 각 소셜 별로 다르게 들어오는 정보를 처리하는 클래스
 */
class OAuthAttributes(
    val nameAttributeKey: String,
    val oAuth2UserInfo: OAuth2UserInfo,
) {

    /**
     * SocialType 에 맞춰서 OAuthAttribute 객체 반환
     */
    companion object {
        fun of(socialType: ProviderType,
               userNameAttributeName: String,
               attributes: MutableMap<String, Any>,
        ): OAuthAttributes? {
            return when (socialType) {
                ProviderType.NAVER -> ofNaver(userNameAttributeName, attributes)
                ProviderType.KAKAO -> ofKakao(userNameAttributeName, attributes)
                ProviderType.GOOGLE -> ofGoogle(userNameAttributeName, attributes)
                ProviderType.NO_SOCIAL -> null
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
    }

    /**
     * ofXXX 메서드로 OAuthAttribute 객체가 생성 되었고, 유저 정보가 담긴 OAuth2UserInfo 가 주입 된 상태
     * OAuth2UserInfo 에서 정보를 가져와 User Entity 반환
     */
    fun toEntity(providerType: ProviderType, oAuth2UserInfo: OAuth2UserInfo): User {
        return User(
            nickname = oAuth2UserInfo.getNickname(),
            providerId = oAuth2UserInfo.getId(),
            providerType = providerType,
            email = oAuth2UserInfo.getEmail(),
            profile = oAuth2UserInfo.getImageUrl(),
            userRole = UserRole.GUEST,
            password = PasswordUtil.generateRandomPassword(),
        )
    }

}