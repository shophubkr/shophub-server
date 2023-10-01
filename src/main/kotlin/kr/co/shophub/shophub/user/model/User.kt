package kr.co.shophub.shophub.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
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
    val providerId: String = "only-social",
    val profile: String = "only-social",

    @Column(name = "is_deleted")
    private var deleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    var userRole: UserRole = UserRole.GUEST_BUYER,

    @Enumerated(EnumType.STRING)
    val providerType: ProviderType = ProviderType.NO_SOCIAL,

) {
    fun encodePassword(encoder: PasswordEncoder) {
        this.password = encoder.encode(password)
    }

    fun updateRefreshToken(updateRefreshToken: String) {
        this.refreshToken = updateRefreshToken
    }

    fun updateEmail(request: SocialJoinRequest) {
        this.email = request.newEmail
        this.userRole = request.role
        this.updateRole()
        this.nickname = request.nickname
        this.password = request.password
    }

    fun updateRole() {
        if (this.userRole == UserRole.GUEST_BUYER) {
            this.userRole = UserRole.USER
        }
        if (this.userRole == UserRole.GUEST_SELLER) {
            this.userRole = UserRole.SELLER
        }
    }

}