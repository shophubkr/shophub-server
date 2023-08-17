package kr.co.shophub.shophub.global.jwt.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

/**
 * jwt 생성, 전송, 처리 서비스
 */
@Service
class JwtService(
    @Value("\${jwt.secretKey}")
    private var secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpirationPeriod: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpirationPeriod: Long,

    @Value("\${jwt.access.header}")
    val accessHeader: String,

    @Value("\${jwt.refresh.header}")
    val refreshHeader: String,
) {

    companion object {
        const val ACCESS_TOKEN_SUBJECT: String = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT: String = "RefreshToken"
        const val EMAIL_CLAIM: String = "email"
        const val BEARER: String = "Bearer "
    }

    fun createAccessToken(email: String): String {
        val now = Date()
        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(Date(now.time + accessTokenExpirationPeriod))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun createRefreshToken(): String {
        val now = Date()
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(Date(now.time + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun sendAccessAndRefreshToken(
        response: HttpServletResponse,
        accessToken: String,
        refreshToken: String?
    ) {
        response.status = HttpServletResponse.SC_OK
        response.setHeader(accessHeader, accessToken)
        response.setHeader(refreshHeader, refreshToken)
    }

    fun extractAccessToken(request: HttpServletRequest): String? {
        val header = request.getHeader(accessHeader) ?: return null
        return if (header.startsWith(BEARER)) {
            header.replace(BEARER, "")
        } else null
    }

    fun extractRefreshToken(request: HttpServletRequest): String? {
        val header = request.getHeader(refreshHeader) ?: return null
        return if (header.startsWith(BEARER)) {
            header.replace(BEARER, "")
        } else null
    }

    fun extractEmail(accessToken: String?): String? {
        return try {
            JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getClaim(EMAIL_CLAIM)
                .asString()
        } catch (e: Exception) {
            null
        }
    }

}