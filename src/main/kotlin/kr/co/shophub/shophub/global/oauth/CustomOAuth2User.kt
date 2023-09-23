package kr.co.shophub.shophub.global.oauth

import kr.co.shophub.shophub.user.model.ProviderType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.DefaultOAuth2User

class CustomOAuth2User(
    authorities: MutableCollection<out GrantedAuthority>?,
    attributes: MutableMap<String, Any>?,
    nameAttributeKey: String?,
    val email: String,
    val type: ProviderType,
) : DefaultOAuth2User(authorities, attributes, nameAttributeKey)