package kr.co.shophub.shophub.user.domain

import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long = 0L,

    val email: String,
    var password: String,
    val nickname: String,
    val providerId: String = "only-social",
    val profile: String = "only-social",
    var isDeleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    val userRole: UserRole = UserRole.GUEST,

    @Enumerated(EnumType.STRING)
    val providerType: ProviderType = ProviderType.NO_SOCIAL,

) {
    fun encodePassword(encoder: PasswordEncoder) {
        this.password = encoder.encode(password)
    }
}