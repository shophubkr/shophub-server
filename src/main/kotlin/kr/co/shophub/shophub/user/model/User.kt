package kr.co.shophub.shophub.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import kr.co.shophub.shophub.global.oauth.OAuthAttributes
import kr.co.shophub.shophub.user.dto.SocialJoinRequest
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long = 0L,

    @field:Email
    var email: String,

    @field:NotNull
    var password: String,

    @field:NotNull
    var nickname: String,

    var refreshToken: String = "empty",
    var providerId: String = "only-social",
    var profile: String = "only-social",
    var telNum: String = "",
    var bizNum: String = "",

    @Column(name = "is_deleted")
    private var deleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    var userRole: UserRole = UserRole.GUEST_BUYER,

    @Enumerated(EnumType.STRING)
    var providerType: ProviderType = ProviderType.NO_SOCIAL,

    ) {
    fun encodePassword(encoder: PasswordEncoder) {
        this.password = encoder.encode(password)
    }

    fun updateRefreshToken(updateRefreshToken: String) {
        this.refreshToken = updateRefreshToken
    }

    fun updateSocialInfo(request: SocialJoinRequest, checkRole: UserRole) {
        this.email = request.newEmail
        this.userRole = checkRole
        this.nickname = request.nickname
        this.password = request.password
        this.telNum = request.phoneNumber
        this.bizNum = request.bizNumber
    }

    fun updateRole() {
        if (this.userRole == UserRole.GUEST_BUYER) {
            this.userRole = UserRole.USER
        }
        if (this.userRole == UserRole.GUEST_SELLER) {
            this.userRole = UserRole.SELLER
        }
    }

    fun mergeInfo(attributes: OAuthAttributes, socialType: ProviderType) {
        val userInfo = attributes.oAuth2UserInfo
        this.profile = userInfo.getImageUrl()
        this.providerId = userInfo.getId()
        this.providerType = socialType
    }

}