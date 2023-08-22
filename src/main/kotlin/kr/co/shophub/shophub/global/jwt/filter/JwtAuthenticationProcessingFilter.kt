package kr.co.shophub.shophub.global.jwt.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.shophub.shophub.global.jwt.service.JwtService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationProcessingFilter(
    private val jwtService: JwtService,
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

        val headerToken = jwtService.extractToken(request)

        if (headerToken != null && jwtService.isTokenValid(headerToken)) {
            val authentication = jwtService.getAuthentication(headerToken)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}