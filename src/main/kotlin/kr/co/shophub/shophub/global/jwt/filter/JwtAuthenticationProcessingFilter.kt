package kr.co.shophub.shophub.global.jwt.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.global.jwt.util.PasswordUtil
import kr.co.shophub.shophub.user.domain.User
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT 인증 필터
 * /login 이외의 URI 요청이 왔을 때 처리
 */
class JwtAuthenticationProcessingFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val authoritiesMapper: GrantedAuthoritiesMapper = NullAuthoritiesMapper(),
) : OncePerRequestFilter() {

    companion object {
        const val NO_CHECK_URL = "/login"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response)
            return
        }

        val refreshToken = jwtService.extractRefreshToken(request)
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken)
            return
        }

        checkAccessTokenAndAuthentication(request, response, filterChain)
    }

    private fun checkRefreshTokenAndReIssueAccessToken(response: HttpServletResponse, refreshToken: String) {
        val user = userRepository.findByRefreshToken(refreshToken)
        if (user != null) {
            val reIssueRefreshToken = reIssueRefreshToken(user)
            val accessToken = jwtService.createAccessToken(user.email)
            jwtService.sendAccessAndRefreshToken(response, accessToken, reIssueRefreshToken)
        }
    }

    private fun reIssueRefreshToken(user: User): String {
        val reIssuedRefreshToken = jwtService.createRefreshToken()
        user.updateRefreshToken(reIssuedRefreshToken)
        userRepository.saveAndFlush(user)
        return reIssuedRefreshToken
    }

    private fun checkAccessTokenAndAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = jwtService.extractAccessToken(request)
        val email = jwtService.extractEmail(accessToken)
        if (email != null) {
            val user = userRepository.findByEmail(email)
            saveAuthentication(user!!)
        }
        filterChain.doFilter(request, response)
    }

    private fun saveAuthentication(user: User) {
        val password = PasswordUtil.generateRandomPassword()
        val userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.email)
            .password(password)
            .roles(user.userRole.name)
            .build()
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            authoritiesMapper.mapAuthorities(userDetails.authorities)
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

}