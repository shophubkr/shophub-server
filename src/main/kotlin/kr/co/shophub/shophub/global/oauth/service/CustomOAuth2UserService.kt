package kr.co.shophub.shophub.global.oauth.service

import kr.co.shophub.shophub.global.oauth.CustomOAuth2User
import kr.co.shophub.shophub.global.oauth.OAuthAttributes
import kr.co.shophub.shophub.user.model.ProviderType
import kr.co.shophub.shophub.user.model.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    companion object {
        const val NAVER = "naver"
        const val KAKAO = "kakao"
        const val GOOGLE = "google"
    }

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {

        val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)
        val registrationId = userRequest.clientRegistration.registrationId
        val socialType = getSocialType(registrationId)
        val userNameAttributeName = getUserNameAttribute(userRequest)
        val attributes = oAuth2User.attributes
        val extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes)

        val user = getUser(extractAttributes!!, socialType)

        return CustomOAuth2User(
            Collections.singleton(SimpleGrantedAuthority(user.userRole.name)),
            attributes,
            extractAttributes.nameAttributeKey,
            user.email,
            socialType,
        )
    }

    private fun getUserNameAttribute(userRequest: OAuth2UserRequest): String =
        userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

    private fun getSocialType(registrationId: String): ProviderType {
        return when (registrationId) {
            NAVER -> ProviderType.NAVER
            KAKAO -> ProviderType.KAKAO
            GOOGLE -> ProviderType.GOOGLE
            else -> ProviderType.NO_SOCIAL
        }
    }

    private fun getUser(attributes: OAuthAttributes, socialType: ProviderType): User {
        return userRepository.findByProviderTypeAndProviderId(
            socialType,
            attributes.oAuth2UserInfo.getId()
        ) ?: return saveUser(attributes, socialType)
    }

    private fun saveUser(attributes: OAuthAttributes, socialType: ProviderType): User {
        val createUser = attributes.toEntity(socialType, attributes.oAuth2UserInfo)
        return userRepository.save(createUser)
    }

}