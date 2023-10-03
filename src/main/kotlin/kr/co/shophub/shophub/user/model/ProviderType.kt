package kr.co.shophub.shophub.user.model

enum class ProviderType(
    val description: String,
) {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    NO_SOCIAL("no_social");
}
