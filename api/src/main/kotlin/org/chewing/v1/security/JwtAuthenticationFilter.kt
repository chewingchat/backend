package org.chewing.v1.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.UnauthorizedException
import org.chewing.v1.implementation.JwtTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
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
            if (token != null) {
                jwtTokenProvider.validateToken(token)  // 토큰 유효성 검사 및 예외 처리
                val userId = jwtTokenProvider.getUserIdFromToken(token)
                val authentication = JwtAuthenticationToken(userId, null, emptyList())
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            // 여기서 예외 발생 시 처리할 로직 추가 가능
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7) // "Bearer " 제거
        } else null
    }



}