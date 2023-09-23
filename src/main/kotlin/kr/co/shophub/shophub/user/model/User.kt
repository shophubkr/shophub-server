package kr.co.shophub.shophub.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long = 0L,

    @field:Email
    val email: String,

    @field:NotNull
    var password: String,

    @field:NotNull
    val nickname: String,

    var refreshToken: String = "empty",
    val providerId: String = "only-social",
    val profile: String = "only-social",

    @Column(name = "is_deleted")
    private var deleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    val userRole: UserRole = UserRole.GUEST,

    @Enumerated(EnumType.STRING)
    val providerType: ProviderType = ProviderType.NO_SOCIAL,

) {
    fun encodePassword(encoder: PasswordEncoder) {
        this.password = encoder.encode(password)
    }

    fun updateRefreshToken(updateRefreshToken: String) {
        this.refreshToken = updateRefreshToken
    }
}