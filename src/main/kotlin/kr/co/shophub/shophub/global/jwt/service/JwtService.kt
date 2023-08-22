package kr.co.shophub.shophub.global.jwt.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.http.HttpServletRequest
import kr.co.shophub.shophub.global.login.service.LoginService
import kr.co.shophub.shophub.user.controller.dto.response.TokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
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

    private val loginService: LoginService,
) {

    companion object {
        const val ACCESS_TOKEN_SUBJECT: String = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT: String = "RefreshToken"
        const val EMAIL_CLAIM: String = "email"
        const val BEARER: String = "Bearer "
        const val JWT_TOKEN: String = "Authorization"
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

    fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader(JWT_TOKEN) ?: return null
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

    // 나중에 에러 처리 구현하면 그때 각각 예외 던져주면 될 듯 합니다!
    fun isTokenValid(token: String?): Boolean {
        if (token == null) {
            return false
        }
        return try {
            JWT.require(Algorithm.HMAC512(secretKey))
                .build().verify(token)
            true
        } catch (e: TokenExpiredException) {
            println("토큰 만료")
            false
        } catch (e: AlgorithmMismatchException) {
            println("알고리즘 불일치")
            false
        } catch (e: SignatureVerificationException) {
            println("서명 불일치")
            false
        } catch (e: JWTVerificationException) {
            println("토큰 문제")
            false
        }
    }

    fun getAuthentication(headerToken: String): Authentication? {
        val email = extractEmail(headerToken)
        return if (email != null) {
            val userDetails = loginService.loadUserByUsername(email)
            UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                NullAuthoritiesMapper().mapAuthorities(userDetails.authorities)
            )
        } else null
    }

    fun getTokenResponse(email: String): TokenResponse {
        val accessToken = createAccessToken(email)
        val refreshToken = createRefreshToken()
        return TokenResponse(accessToken, refreshToken)
    }

}