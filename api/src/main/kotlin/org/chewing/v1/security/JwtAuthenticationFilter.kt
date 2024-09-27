package org.chewing.v1.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.auth.JwtTokenProvider
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*


@Component
@Profile("!test")
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 여기부터 수정
        try {
            val token = resolveToken(request)
            jwtTokenProvider.validateToken(token)  // 토큰 유효성 검사 및 예외 처리
            val userId = jwtTokenProvider.getUserIdFromToken(token)
            request.setAttribute("userId", userId)
        } catch (e: AuthorizationException) {
            request.setAttribute("Exception", e)
        }
        filterChain.doFilter(request, response)
    }

    @Throws(ServletException::class)
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        // 특정 경로를 무시하도록 설정
        return path.startsWith("/api/auth") && !path.startsWith("/api/auth/phone/update/") && !path.startsWith("/api/auth/email/update/")
    }


    private fun resolveToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            jwtTokenProvider.cleanedToken(bearerToken)
        } else throw AuthorizationException(ErrorCode.INVALID_TOKEN)
    }
}