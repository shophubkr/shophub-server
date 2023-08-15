package kr.co.shophub.shophub.user.domain

import jakarta.persistence.*

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long = 0L,

    val email: String,
    val password: String,
    val nickname: String,
    val providerId: String,
    val profile: String,
    val isDeleted: Boolean,

    @Enumerated(EnumType.STRING)
    val userRole: UserRole,

    @Enumerated(EnumType.STRING)
    val providerType: ProviderType = ProviderType.NO_SOCIAL,

)